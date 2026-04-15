# Основні можливості Hibernate та Spring Data JPA

## Зміст
1. [Вступ](#вступ)
2. [Що таке JPA і де тут Hibernate та Spring Data JPA](#що-таке-jpa-і-де-тут-hibernate-та-spring-data-jpa)
3. [Hibernate: що це таке](#hibernate-що-це-таке)
4. [Основні можливості Hibernate](#основні-можливості-hibernate)
   - [ORM-мапінг](#1-orm-мапінг-object-relational-mapping)
   - [Робота із сутностями та життєвий цикл entity](#2-робота-із-сутностями-та-життєвий-цикл-entity)
   - [Керування зв’язками між сутностями](#3-керування-звязками-між-сутностями)
   - [Fetch strategies: Lazy та Eager](#4-fetch-strategies-lazy-та-eager)
   - [Кешування](#5-кешування)
   - [Dirty checking](#6-dirty-checking)
   - [Транзакційність](#7-транзакційність)
   - [HQL, JPQL, Criteria API](#8-hql-jpql-criteria-api)
   - [Pagination та sorting](#9-pagination-та-sorting)
   - [Batch processing](#10-batch-processing)
   - [Каскадні операції](#11-каскадні-операції)
   - [Оптимістичне блокування](#12-оптимістичне-блокування)
   - [Підтримка inheritance](#13-підтримка-inheritance)
   - [Native SQL](#14-native-sql)
5. [Spring Data JPA: що це таке](#spring-data-jpa-що-це-таке)
6. [Основні можливості Spring Data JPA](#основні-можливості-spring-data-jpa)
   - [Репозиторії](#1-репозиторії)
   - [Генерація запитів із назв методів](#2-генерація-запитів-із-назв-методів)
   - [Анотація @Query](#3-анотація-query)
   - [Пагінація і сортування](#4-пагінація-і-сортування)
   - [Specifications](#5-specifications)
   - [Проєкції](#6-проєкції)
   - [Auditing](#7-auditing)
   - [Кастомні репозиторії](#8-кастомні-репозиторії)
7. [Порівняння Hibernate та Spring Data JPA](#порівняння-hibernate-та-spring-data-jpa)
8. [Як вони працюють разом](#як-вони-працюють-разом)
9. [Типові проблеми і підводні камені](#типові-проблеми-і-підводні-камені)
10. [Коли що краще використовувати](#коли-що-краще-використовувати)
11. [Висновок](#висновок)

---

## Вступ

У Java-розробці для роботи з реляційними базами даних дуже часто використовують **Hibernate** та **Spring Data JPA**. Обидві технології пов’язані з доступом до даних, але вони виконують різні ролі:

- **Hibernate** — це ORM-фреймворк, який реалізує JPA та надає багато додаткових можливостей.
- **Spring Data JPA** — це надбудова над JPA, яка значно спрощує написання DAO/Repository-рівня в застосунках на Spring.

На практиці вони дуже часто використовуються **разом**:
- Spring Data JPA дає зручний API для репозиторіїв;
- Hibernate зазвичай виступає як JPA provider, який реально виконує мапінг об’єктів у таблиці та генерує SQL.

---

## Що таке JPA і де тут Hibernate та Spring Data JPA

**JPA (Java Persistence API)** — це специфікація Java для роботи з персистентністю даних. Вона визначає:
- як описувати сутності;
- як працювати з `EntityManager`;
- як виконувати запити;
- як керувати життєвим циклом об’єктів.

Важливо розуміти:

- **JPA** — це не готова бібліотека, а стандарт.
- **Hibernate** — це одна з найпопулярніших реалізацій JPA.
- **Spring Data JPA** — це інструмент, який спрощує використання JPA в Spring-застосунках.

Тобто спрощено:

- **JPA** = правила;
- **Hibernate** = механізм, що ці правила реалізує;
- **Spring Data JPA** = зручна надбудова для повсякденної роботи.

---

## Hibernate: що це таке

**Hibernate** — це ORM-фреймворк для Java, який дозволяє працювати з базою даних через Java-об’єкти замість ручного написання великої кількості SQL.

Ідея ORM полягає в тому, що:
- таблиця бази даних відповідає класу;
- рядок таблиці відповідає об’єкту;
- колонки таблиці відповідають полям класу.

Наприклад, таблиця `students` може бути відображена на клас `Student`.

```java
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    private String email;
}
```

Hibernate дозволяє:
- зберігати об’єкти в БД;
- оновлювати їх;
- видаляти;
- виконувати пошук;
- працювати зі зв’язками між сутностями;
- оптимізувати доступ до даних.

---

## Основні можливості Hibernate

## 1. ORM-мапінг (Object-Relational Mapping)

Головна можливість Hibernate — це мапінг Java-об’єктів на таблиці БД.

За допомогою анотацій можна описати:
- назву таблиці;
- первинний ключ;
- назви колонок;
- обмеження;
- типи зв’язків;
- стратегії наслідування.

Типові анотації:
- `@Entity`
- `@Table`
- `@Id`
- `@GeneratedValue`
- `@Column`
- `@Transient`
- `@Enumerated`
- `@Embeddable`
- `@Embedded`

Приклад вбудованого об’єкта:

```java
@Embeddable
public class Address {
    private String city;
    private String street;
}

@Entity
public class Teacher {
    @Id
    private Long id;

    @Embedded
    private Address address;
}
```

Це дозволяє будувати багатші доменні моделі без ручного мапінгу кожного SQL-запиту.

## 2. Робота із сутностями та життєвий цикл Entity

Hibernate керує станами сутностей. Основні стани:

- **Transient** — об’єкт створений у Java, але ще не збережений у БД.
- **Persistent / Managed** — об’єкт приєднаний до persistence context і відслідковується Hibernate.
- **Detached** — об’єкт був persistent, але більше не керується поточним контекстом.
- **Removed** — об’єкт позначений на видалення.

Приклад:

```java
Student student = new Student(); // transient
student.setFullName("Ivan Petrenko");

entityManager.persist(student);   // persistent
```

Розуміння цих станів дуже важливе для коректної роботи із транзакціями, оновленнями та merge-операціями.

## 3. Керування зв’язками між сутностями

Hibernate підтримує всі базові типи зв’язків:

- `@OneToOne`
- `@OneToMany`
- `@ManyToOne`
- `@ManyToMany`

Приклад зв’язку "багато студентів належать одній групі":

```java
@Entity
public class Student {
    @Id
    private Long id;

    private String fullName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private StudentGroup group;
}
```

Приклад зворотного боку:

```java
@Entity
public class StudentGroup {
    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "group")
    private List<Student> students = new ArrayList<>();
}
```

Hibernate бере на себе значну частину логіки мапінгу таких зв’язків і дозволяє працювати з ними як з об’єктною моделлю.

## 4. Fetch strategies: Lazy та Eager

Hibernate дозволяє керувати тим, **коли** завантажуються пов’язані об’єкти.

### `FetchType.LAZY`
Пов’язані дані завантажуються лише тоді, коли вони реально потрібні.

Плюс:
- менше зайвих даних у пам’яті;
- краща продуктивність у багатьох сценаріях.

Мінус:
- можна отримати `LazyInitializationException`, якщо звернутися до lazy-поля поза транзакцією.

### `FetchType.EAGER`
Пов’язані дані завантажуються одразу.

Плюс:
- простіше в деяких сценаріях.

Мінус:
- ризик зайвих JOIN-ів і перевантаження даними.

На практиці часто рекомендують:
- за замовчуванням схилятися до `LAZY`;
- явно керувати завантаженням через `join fetch`, `EntityGraph` або спеціальні запити.

## 5. Кешування

Hibernate підтримує кілька рівнів кешу.

### First-level cache
- існує на рівні `Session` / persistence context;
- увімкнений завжди;
- якщо сутність уже завантажена в межах поточної сесії, Hibernate не робить повторний SQL-запит.

### Second-level cache
- спільний між сесіями;
- потребує окремої конфігурації;
- корисний для даних, які часто читаються і рідко змінюються.

### Query cache
- кешує результати запитів;
- застосовується обережно, бо не завжди дає виграш.

Кешування може суттєво покращити продуктивність, але потребує грамотного налаштування.

## 6. Dirty checking

Однією з дуже зручних можливостей Hibernate є **dirty checking**.

Суть у тому, що якщо сутність перебуває у managed-стані, Hibernate відслідковує зміну її полів і наприкінці транзакції сам виконує `UPDATE`, якщо це потрібно.

```java
@Transactional
public void updateStudentName(Long id, String newName) {
    Student student = entityManager.find(Student.class, id);
    student.setFullName(newName);
}
```

У цьому прикладі явний виклик `update()` не потрібний: Hibernate сам помітить зміну поля `fullName`.

Переваги:
- менше шаблонного коду;
- більш природна робота з доменною моделлю.

## 7. Транзакційність

Hibernate тісно працює з транзакціями. У Spring-застосунках найчастіше використовують `@Transactional`.

Транзакції потрібні для:
- атомарності змін;
- узгодженості даних;
- коректної роботи lazy loading;
- безпечного виконання групи операцій.

Приклад:

```java
@Transactional
public void createStudentAndAssignGroup(Student student, Long groupId) {
    StudentGroup group = entityManager.find(StudentGroup.class, groupId);
    student.setGroup(group);
    entityManager.persist(student);
}
```

Без транзакцій ORM-рівень часто працює некоректно або не так, як очікується.

## 8. HQL, JPQL, Criteria API

Hibernate надає кілька способів формувати запити.

### HQL / JPQL
Це мови запитів, схожі на SQL, але вони працюють із сутностями та їх полями, а не напряму з таблицями.

```java
List<Student> students = entityManager
    .createQuery("select s from Student s where s.fullName like :name", Student.class)
    .setParameter("name", "%Ivan%")
    .getResultList();
```

### Criteria API
Дозволяє будувати запити програмно.

Переваги:
- зручно для динамічних фільтрів;
- type-safe на рівні API.

Недоліки:
- код часто більш громіздкий і менш читабельний.

## 9. Pagination та sorting

Hibernate підтримує:
- посторінкову вибірку;
- сортування результатів.

Наприклад:

```java
List<Student> students = entityManager
    .createQuery("select s from Student s order by s.fullName", Student.class)
    .setFirstResult(0)
    .setMaxResults(20)
    .getResultList();
```

Це критично для великих таблиць, де не можна читати всі дані одразу.

## 10. Batch processing

Hibernate підтримує пакетну обробку вставок та оновлень.

Це особливо важливо, коли потрібно:
- імпортувати великі обсяги даних;
- виконувати масові оновлення;
- зменшити кількість round-trip до БД.

Типові інструменти:
- batch inserts;
- batch updates;
- `flush()` та `clear()` у циклах;
- налаштування `hibernate.jdbc.batch_size`.

Приклад ідеї:

```java
for (int i = 0; i < students.size(); i++) {
    entityManager.persist(students.get(i));

    if (i % 50 == 0) {
        entityManager.flush();
        entityManager.clear();
    }
}
```

## 11. Каскадні операції

Hibernate дозволяє автоматично поширювати операції на пов’язані сутності.

Наприклад:
- `CascadeType.PERSIST`
- `CascadeType.MERGE`
- `CascadeType.REMOVE`
- `CascadeType.ALL`

```java
@OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
private List<Student> students;
```

Це зручно, але з каскадами треба бути обережним, особливо з `REMOVE`, щоб випадково не видалити зайві дані.

## 12. Оптимістичне блокування

Hibernate підтримує механізм **optimistic locking** через `@Version`.

```java
@Entity
public class Course {
    @Id
    private Long id;

    @Version
    private Long version;

    private String name;
}
```

Це дозволяє виявляти конфлікти конкурентних оновлень:
- якщо два користувачі одночасно редагують той самий запис,
- при збереженні другого оновлення можна отримати помилку конфлікту версій.

Це дуже корисно в багатокористувацьких системах.

## 13. Підтримка inheritance

Hibernate підтримує кілька стратегій наслідування:
- `SINGLE_TABLE`
- `JOINED`
- `TABLE_PER_CLASS`

Приклад:

```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person {
    @Id
    private Long id;

    private String name;
}

@Entity
public class Teacher extends Person {
    private String department;
}
```

Це дозволяє будувати складніші об’єктні моделі, хоча потребує акуратного проєктування структури таблиць.

## 14. Native SQL

Hibernate не обмежує розробника лише ORM-механізмами. За потреби можна використовувати native SQL.

Це корисно коли:
- потрібен дуже специфічний SQL;
- використовуються особливості конкретної СУБД;
- ORM-запит виходить надто неефективним або складним.

```java
List<Object[]> rows = entityManager
    .createNativeQuery("SELECT id, full_name FROM students WHERE full_name LIKE ?")
    .setParameter(1, "%Ivan%")
    .getResultList();
```

Таким чином Hibernate дає баланс між високорівневим ORM і можливістю опускатися на нижчий рівень.

---

## Spring Data JPA: що це таке

**Spring Data JPA** — це частина екосистеми Spring Data, яка спрощує роботу з JPA.

Головна ідея Spring Data JPA: **мінімізувати кількість шаблонного коду для доступу до даних**.

Без Spring Data JPA розробник часто вручну пише:
- DAO-класи;
- `EntityManager`-логіку;
- типові CRUD-операції;
- прості пошукові запити.

Spring Data JPA дозволяє винести значну частину цієї рутини в інтерфейси репозиторіїв.

---

## Основні можливості Spring Data JPA

## 1. Репозиторії

Основою Spring Data JPA є інтерфейси репозиторіїв.

Найчастіше використовують:
- `CrudRepository`
- `PagingAndSortingRepository`
- `JpaRepository`

Приклад:

```java
public interface StudentRepository extends JpaRepository<Student, Long> {
}
```

Після цього Spring автоматично надає реалізацію стандартних методів:
- `save()`
- `findById()`
- `findAll()`
- `deleteById()`
- `count()`
- `existsById()`

Це різко зменшує кількість коду.

## 2. Генерація запитів із назв методів

Одна з найвідоміших можливостей Spring Data JPA — автоматичне створення запитів за назвою методу.

Наприклад:

```java
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByFullName(String fullName);
    List<Student> findByEmailContaining(String fragment);
    List<Student> findByGroupName(String groupName);
    boolean existsByEmail(String email);
}
```

Spring Data JPA аналізує назву методу і генерує відповідний JPQL/SQL.

Популярні ключові слова:
- `findBy`
- `readBy`
- `getBy`
- `existsBy`
- `countBy`
- `Containing`
- `StartingWith`
- `EndingWith`
- `Between`
- `LessThan`
- `GreaterThan`
- `OrderBy`
- `And`
- `Or`
- `In`
- `IsNull`
- `True` / `False`

Це дуже зручно для простих і середньої складності запитів.

## 3. Анотація `@Query`

Коли назви методу вже недостатньо або запит складніший, можна явно задати JPQL чи native SQL через `@Query`.

```java
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select s from Student s where lower(s.fullName) like lower(concat('%', :namePart, '%'))")
    List<Student> searchByName(@Param("namePart") String namePart);
}
```

Також підтримується native query:

```java
@Query(value = "SELECT * FROM students WHERE email LIKE %:emailPart%", nativeQuery = true)
List<Student> findByEmailPartNative(@Param("emailPart") String emailPart);
```

Перевага `@Query` у тому, що логіка запиту явно видима в одному місці.

## 4. Пагінація і сортування

Spring Data JPA має дуже зручну підтримку пагінації та сортування через `Pageable`, `Page`, `Slice`, `Sort`.

```java
public interface StudentRepository extends JpaRepository<Student, Long> {
    Page<Student> findByFullNameContaining(String namePart, Pageable pageable);
}
```

Використання:

```java
Page<Student> page = studentRepository.findByFullNameContaining(
    "Ivan",
    PageRequest.of(0, 20, Sort.by("fullName").ascending())
);
```

Це дуже корисно для REST API, таблиць у UI та великих наборів даних.

## 5. Specifications

Spring Data JPA підтримує **Specifications** для побудови динамічних умов пошуку.

Це зручно, коли фільтри:
- необов’язкові;
- комбінуються в різних варіантах;
- формуються на основі параметрів запиту користувача.

Приклад репозиторію:

```java
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
}
```

Після цього можна будувати специфікації окремими класами або методами.

Переваги:
- добра масштабованість для складних фільтрів;
- чистіший код, ніж велика кількість методів `findBy...`.

## 6. Проєкції

Spring Data JPA дозволяє повертати не всю сутність, а лише частину даних.

Це називається **projection**.

### Interface-based projection

```java
public interface StudentSummary {
    String getFullName();
    String getEmail();
}
```

```java
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<StudentSummary> findByFullNameContaining(String name);
}
```

Переваги:
- менше даних передається з БД;
- зручніше для read-only сценаріїв;
- інколи краща продуктивність.

## 7. Auditing

Spring Data JPA підтримує аудит сутностей, тобто автоматичне заповнення службових полів.

Типові анотації:
- `@CreatedDate`
- `@LastModifiedDate`
- `@CreatedBy`
- `@LastModifiedBy`

Приклад:

```java
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Student {

    @Id
    private Long id;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
```

Це зручно для бізнес-систем, де важливо знати:
- коли створено запис;
- коли востаннє змінено;
- ким створено або змінено.

## 8. Кастомні репозиторії

Якщо стандартних можливостей недостатньо, Spring Data JPA дозволяє додавати власну реалізацію частини репозиторію.

Наприклад:
- складні Criteria-запити;
- використання `EntityManager` напряму;
- batch-операції;
- спеціальна оптимізація читання/запису.

Зазвичай це поєднує зручність Spring Data JPA з гнучкістю чистого Hibernate/JPA.

---

## Порівняння Hibernate та Spring Data JPA

| Критерій | Hibernate | Spring Data JPA |
|---|---|---|
| Роль | ORM-фреймворк / JPA provider | Надбудова для спрощення роботи з JPA |
| Основна задача | Мапінг об’єктів, керування persistence context, генерація SQL | Спрощення repository-рівня |
| Рівень абстракції | Нижчий | Вищий |
| CRUD | Є, але більш вручну | Дуже зручно через репозиторії |
| Динамічні запити | JPQL, HQL, Criteria API | Method naming, `@Query`, Specifications |
| Гнучкість | Дуже висока | Висока, але в межах підходу репозиторіїв |
| Обсяг шаблонного коду | Більший | Менший |
| Контроль над ORM | Максимальний | Частково прихований за abstractions |

Коротко:
- **Hibernate** дає глибокий контроль і ширші ORM-можливості;
- **Spring Data JPA** дає швидкість розробки і простіший код.

---

## Як вони працюють разом

У типовому Spring Boot-застосунку взаємодія виглядає так:

1. Ви створюєте `@Entity`-класи.
2. Ви створюєте інтерфейси репозиторіїв Spring Data JPA.
3. Усередині Spring Data JPA для виконання операцій використовується JPA.
4. Конкретним JPA provider зазвичай є Hibernate.
5. Hibernate перетворює операції репозиторію на SQL-запити до БД.

Тобто коли ви викликаєте:

```java
studentRepository.findById(1L);
```

насправді:
- Spring Data JPA надає реалізацію репозиторію;
- Hibernate виконує завантаження сутності;
- JDBC-драйвер відправляє SQL у базу даних.

---

## Типові проблеми і підводні камені

Попри зручність Hibernate та Spring Data JPA, є низка типових проблем.

### 1. N+1 problem
Коли спочатку завантажується список сутностей, а потім для кожної окремо підтягуються пов’язані дані.

Наслідок:
- велика кількість SQL-запитів;
- погіршення продуктивності.

Типові рішення:
- `join fetch`;
- `EntityGraph`;
- DTO/projection;
- перегляд fetch strategy.

### 2. `LazyInitializationException`
Виникає, коли lazy-поле читається вже після завершення persistence context / транзакції.

Рішення:
- правильно визначати межі транзакцій;
- заздалегідь завантажувати потрібні зв’язки;
- використовувати DTO.

### 3. Завеликі сутності та складні графи об’єктів
Надто складна модель з великою кількістю двобічних зв’язків може призводити до:
- складних SQL;
- рекурсій під час серіалізації;
- труднощів у супроводі.

### 4. Надмірне використання `EAGER`
Це може різко збільшити кількість JOIN-ів та обсяг даних.

### 5. Некоректне використання каскадів
Особливо небезпечний `CascadeType.REMOVE`, якщо модель зв’язків спроєктована неакуратно.

### 6. Великі назви методів у Spring Data JPA
Методи на кшталт:

```java
findByStatusAndTypeAndCreatedAtBetweenAndOwnerIdOrderByCreatedAtDesc(...)
```

формально працюють, але погіршують читабельність. У таких випадках краще використовувати `@Query`, Specification або кастомну реалізацію.

---

## Коли що краще використовувати

### Hibernate варто добре знати, якщо:
- потрібне глибоке розуміння ORM;
- потрібно оптимізувати складні запити;
- є нетипові мапінги;
- потрібен контроль над persistence context;
- потрібні batch-операції, кешування, locking, inheritance.

### Spring Data JPA особливо зручний, якщо:
- ви працюєте зі Spring Boot;
- потрібно швидко реалізувати CRUD і пошук;
- важлива мінімізація шаблонного коду;
- застосунок має типовий repository/service/controller поділ;
- складність запитів середня або помірна.

### Найкращий практичний підхід
У більшості реальних проєктів доцільно:
- використовувати **Spring Data JPA** для стандартних CRUD-операцій;
- використовувати **Hibernate/JPA API** або кастомні репозиторії там, де потрібен тонкий контроль і оптимізація.

Тобто не варто протиставляти ці технології: зазвичай вони **доповнюють одна одну**.

---

## Висновок

**Hibernate** — це потужний ORM-фреймворк, який забезпечує:
- мапінг Java-об’єктів на таблиці;
- роботу із сутностями та їх зв’язками;
- lazy/eager loading;
- кешування;
- dirty checking;
- транзакційність;
- JPQL/HQL/Criteria API;
- блокування, каскади, inheritance, batch processing.

**Spring Data JPA** — це зручна надбудова, яка:
- спрощує доступ до даних;
- прибирає багато шаблонного коду;
- дає репозиторії з готовими CRUD-операціями;
- дозволяє генерувати запити з назв методів;
- підтримує `@Query`, пагінацію, сортування, Specifications, auditing, projections.

Найважливіша практична думка така:

- **Spring Data JPA** робить розробку швидшою та зручнішою;
- **Hibernate** забезпечує реальну ORM-логіку та глибокі можливості під капотом.

Для сильного Java/Spring-розробника бажано добре розуміти **обидва інструменти**: 
- Spring Data JPA — на рівні зручного повсякденного використання;
- Hibernate — на рівні механізмів, продуктивності та тонкого налаштування.

---

## Коротке резюме в одному абзаці

Hibernate — це ORM-фреймворк і одна з основних реалізацій JPA, який відповідає за мапінг сутностей, життєвий цикл об’єктів, роботу зі зв’язками, транзакціями, кешуванням і генерацією SQL. Spring Data JPA — це надбудова для Spring, яка дозволяє значно спростити роботу з репозиторіями, CRUD-операціями, пагінацією, сортуванням і пошуковими запитами. У типовому Spring Boot-проєкті вони зазвичай працюють разом: Spring Data JPA дає зручний API, а Hibernate виконує основну persistence-логіку під капотом.
