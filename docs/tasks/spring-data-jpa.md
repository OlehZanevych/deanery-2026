# Домашні завдання — Spring Data JPA та Hibernate
### Проект: Deanery 2026

---

## Завдання 1 — Додавання нової сутності: Студент (`Student`)

Додайте до проекту повноцінну підтримку сутності **Student** (студент).

**Що потрібно зробити:**

1. Додати таблицю `students` до `schema.sql` та `src/test/resources/db/schema.sql`:
   ```sql
   CREATE TABLE students (
       id             BIGSERIAL PRIMARY KEY,
       first_name     VARCHAR(64)  NOT NULL,
       middle_name    VARCHAR(64)  NOT NULL,
       last_name      VARCHAR(64)  NOT NULL,
       email          VARCHAR(64)  NOT NULL UNIQUE,
       phone          VARCHAR(32)  NOT NULL,
       group_id       BIGINT REFERENCES academic_groups(id) NOT NULL,
       enrollment_year INTEGER     NOT NULL,
       info           TEXT
   );
   ```
2. Додати тестові дані до `data.sql` (мінімум 10 записів у різних групах).
3. Створити JPA-сутність `StudentEntity` з анотаціями `@Entity`, `@Table`, `@ManyToOne(fetch = FetchType.LAZY)` до `AcademicGroupEntity`.
4. Створити `StudentRepository extends JpaRepository` з:
   - похідними методами: `findByGroupId`, `findByLastNameContainingIgnoreCase`, `existsByEmail`
   - HQL-запитом для пошуку студентів по спеціальності через ланцюжок: `student → group → curriculum → speciality`
   - `@Modifying` HQL для видалення: `DELETE FROM StudentEntity s WHERE s.group.id = :groupId`
5. Створити DTO (`BaseStudentDto`, `StudentDto`), mapper, service та REST-контролер.

---

## Завдання 2 — Пагінація та сортування для наявних ендпоінтів

Додайте підтримку **пагінації та сортування** до контролерів `SpecialityController` та `LecturerController`.

**Що потрібно зробити:**

1. У `SpecialityRepository` додати метод:
   ```java
   Page<SpecialityEntity> findAll(Pageable pageable);
   ```
2. У `LecturerRepository` додати метод:
   ```java
   Page<SpecialityEntity> findByDepartmentId(Long departmentId, Pageable pageable);
   ```
3. Оновити відповідні сервіси та контролери, щоб `GET /specialities` і `GET /lecturers/by-department/{id}` повертали `Page<T>` і приймали параметри `?page=`, `?size=`, `?sort=`.
4. Перевірити роботу наступних URL у Swagger:
   - `GET /specialities?page=0&size=3&sort=name,asc`
   - `GET /lecturers/by-department/7?page=0&size=2&sort=lastName,desc`

> **Підказка:** Використайте `@PageableDefault` для визначення значень за замовчуванням.

---

## Завдання 3 — Проєкції (Projections) для легких відповідей

Реалізуйте **interface-based projections** для сутностей `Lecturer` та `AcademicGroup`.

**Що потрібно зробити:**

1. Створити інтерфейс `LecturerSummaryProjection`:
   ```java
   public interface LecturerSummaryProjection {
       Long getId();
       String getFirstName();
       String getLastName();
       String getTitle();
   }
   ```
2. Додати до `LecturerRepository` метод:
   ```java
   List<LecturerSummaryProjection> findSummaryByDepartmentId(Long departmentId);
   ```
3. Аналогічно створити `AcademicGroupSummaryProjection` з полями `id`, `name`, `enrollmentYear`.
4. Додати відповідні ендпоінти `GET /lecturers/by-department/{id}/summary` та `GET /academic-groups/by-curriculum/{id}/summary`.
5. Порівняти у логах (через `datasource-proxy`) кількість SELECT-запитів до БД для звичайного методу і projection-методу.

---

## Завдання 4 — Власний репозиторій з `EntityManager` (Custom Repository)

Реалізуйте **кастомний репозиторій** для сутності `Course` із використанням `EntityManager` та динамічними фільтрами.

**Що потрібно зробити:**

1. Створити інтерфейс `CourseCustomRepository`:
   ```java
   public interface CourseCustomRepository {
       List<CourseEntity> findWithFilters(String name, Integer semester,
                                         Integer minCredits, Long curriculumId);
   }
   ```
2. Реалізувати `CourseCustomRepositoryImpl` через `EntityManager` та JPQL `TypedQuery`:
   ```java
   @PersistenceContext
   private EntityManager entityManager;
   ```
   Динамічно будуйте HQL-рядок залежно від того, які фільтри передано (не `null`).
3. Змусити `CourseRepository` наслідувати обидва інтерфейси:
   ```java
   public interface CourseRepository
           extends JpaRepository<CourseEntity, Long>, CourseCustomRepository { ... }
   ```
4. Додати ендпоінт `GET /courses/filter?name=&semester=&minCredits=&curriculumId=` до `CourseController`.

---

## Завдання 5 — Аудит сутностей (`@CreatedDate`, `@LastModifiedDate`)

Додайте **автоматичний аудит** (дата створення та остання зміна) до сутностей `CourseEntity` та `LecturerEntity`.

**Що потрібно зробити:**

1. Увімкнути Spring Data JPA Auditing у конфігурації:
   ```java
   @Configuration
   @EnableJpaAuditing
   public class JpaConfig { }
   ```
2. Додати до таблиць `courses` і `lecturers` нові стовпці в `schema.sql`:
   ```sql
   created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
   updated_at  TIMESTAMP NOT NULL DEFAULT NOW()
   ```
3. Оновити `CourseEntity` та `LecturerEntity`, додавши поля:
   ```java
   @CreatedDate
   @Column(nullable = false, updatable = false)
   private LocalDateTime createdAt;

   @LastModifiedDate
   @Column(nullable = false)
   private LocalDateTime updatedAt;
   ```
   Клас повинен бути позначений `@EntityListeners(AuditingEntityListener.class)`.
4. Переконатися, що `createdAt` і `updatedAt` повертаються у відповідях відповідних DTO.
5. Через Swagger: створіть курс, потім оновіть його через `PATCH` і перевірте, що `updatedAt` змінився, а `createdAt` — ні.

---

## Завдання 6 — Каскадне видалення та `@OneToMany`

Додайте **зворотні колекції** (`@OneToMany`) до `CurriculumEntity` та реалізуйте каскадне видалення.

**Що потрібно зробити:**

1. Додати до `CurriculumEntity` поля:
   ```java
   @OneToMany(mappedBy = "curriculum", cascade = CascadeType.REMOVE, orphanRemoval = true)
   private List<CourseEntity> courses = new ArrayList<>();

   @OneToMany(mappedBy = "curriculum", cascade = CascadeType.REMOVE, orphanRemoval = true)
   private List<AcademicGroupEntity> groups = new ArrayList<>();
   ```
2. У `CurriculumServiceImpl.delete(Long id)` перейти від `@Modifying` HQL до стандартного `deleteById()` — і переконатися, що курси та групи видаляються автоматично завдяки каскаду.
3. Написати тест `CurriculumServiceTest`, який:
   - створює curriculum із кількома courses і groups
   - видаляє curriculum
   - перевіряє через `CourseRepository.existsById()` і `AcademicGroupRepository.existsById()`, що дочірні записи теж видалені

> **Увага:** Порівняйте кількість SQL-запитів при каскадному видаленні через JPA та при `@Modifying` HQL-видаленні. Що ефективніше?

---

## Завдання 7 — Іменовані запити (`@NamedQuery`)

Продемонструйте використання `@NamedQuery` як альтернативи до `@Query`.

**Що потрібно зробити:**

1. Додати до `SpecialityEntity` іменовані запити:
   ```java
   @NamedQuery(
       name = "SpecialityEntity.findAllByDepartment",
       query = "SELECT s FROM SpecialityEntity s WHERE s.department.id = :departmentId ORDER BY s.name ASC"
   )
   @NamedQuery(
       name = "SpecialityEntity.countAll",
       query = "SELECT COUNT(s) FROM SpecialityEntity s"
   )
   ```
2. Додати до `SpecialityRepository` відповідні методи, що посилаються на ці іменовані запити (Spring Data JPA знайде їх автоматично за конвенцією `EntityName.methodName`).
3. Переконатися, що ендпоінт `GET /specialities/by-department/{id}` використовує саме `@NamedQuery`.
4. Написати unit-тест із `@DataJpaTest` для перевірки цих запитів.

---

## Завдання 8 — Оптимізація N+1: `@EntityGraph`

Вирішіть **проблему N+1 запитів** у `LecturerController` за допомогою `@EntityGraph`.

**Що потрібно зробити:**

1. Запустити `GET /lecturers` і в логах порахувати кількість SQL-запитів (через `datasource-proxy`). Переконатися, що виникає N+1 при доступі до `department`.
2. Додати до `LecturerRepository` метод із `@EntityGraph`:
   ```java
   @EntityGraph(attributePaths = {"department"})
   List<LecturerEntity> findAllWithDepartment();
   ```
3. Оновити `LecturerService.findAll()` для використання нового методу.
4. Знову запустити `GET /lecturers` і переконатися, що виконується **один** JOIN-запит замість N+1.
5. Написати коментар у коді, що пояснює різницю між `@EntityGraph` та `JOIN FETCH` у `@Query`.

---

## Завдання 9 — Транзакції та рівні ізоляції

Вивчіть поведінку `@Transactional` при різних налаштуваннях.

**Що потрібно зробити:**

1. Реалізуйте в `CourseService` метод `bulkCreateCourses(List<BaseCourseDto> courses)`, який зберігає список курсів в одній транзакції. Якщо хоча б один курс порушує обмеження БД — вся транзакція повинна відкотитися (`rollback`).
2. Напишіть тест, що перевіряє: якщо другий елемент списку має `curriculumId = 9999` (неіснуючий), жоден курс зі списку не зберігається.
3. Додайте метод `getCourseCountForReport(Long curriculumId)` і позначте його `@Transactional(readOnly = true)`. Поясніть у коментарі, чому `readOnly = true` підвищує продуктивність.
4. Додайте до `application.properties` рядок для логування рівня транзакцій:
   ```properties
   logging.level.org.springframework.transaction=TRACE
   ```
   Запустіть кілька запитів і проаналізуйте лог.

---

## Завдання 10 — Тести з `@DataJpaTest` та embedded PostgreSQL

Напишіть набір **інтеграційних тестів** для `CourseRepository` та `SpecialityRepository`.

**Що потрібно зробити:**

1. Створити тест `CourseRepositoryTest` з анотаціями:
   ```java
   @DataJpaTest
   @AutoConfigureEmbeddedDatabase(type = DatabaseType.POSTGRES)
   @Sql("/db/schema.sql")
   ```
2. Написати тести для наступних сценаріїв:
   - `findByCurriculumId` повертає правильну кількість курсів
   - `searchByName("програм")` повертає лише курси, назва яких містить "програм" (без урахування регістру)
   - `removeById(id)` повертає `1` для існуючого запису і `0` для неіснуючого
   - `findByMinCredits(5)` повертає лише курси з `credits >= 5`
   - `countByCurriculumId` повертає коректне число
3. Створити тест `SpecialityRepositoryTest`:
   - перевірити `findByFacultyId` (HQL з навігацією через кілька рівнів)
   - перевірити `existsByCode` та `existsByName`
   - перевірити `countByDepartmentId`
4. Використати `@Sql` для підготовки тестових даних у кожному тест-методі (`@Sql(scripts = {...}, executionPhase = BEFORE_TEST_METHOD)`).

---

## Загальні вимоги до виконання

- Весь новий код оформити у відповідних пакетах (за прикладом існуючої структури проекту).
- Кожен новий публічний метод у сервісі та репозиторії повинен мати JavaDoc-коментар.
- Для кожного завдання, де є тести, покриття нових класів повинно бути не менше **70%** (перевірити через `mvn test jacoco:report`).
- Перед здачею переконатися, що `mvn clean package` виконується **без помилок**.
