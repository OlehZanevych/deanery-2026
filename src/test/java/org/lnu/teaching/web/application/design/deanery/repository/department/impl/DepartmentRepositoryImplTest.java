package org.lnu.teaching.web.application.design.deanery.repository.department.impl;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.lnu.teaching.web.application.design.deanery.entity.department.DepartmentEntity;
import org.lnu.teaching.web.application.design.deanery.entity.faculty.FacultyEntity;
import org.lnu.teaching.web.application.design.deanery.repository.faculty.impl.FacultyRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Sql("/db/schema.sql")
@AutoConfigureEmbeddedDatabase(type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES)
class DepartmentRepositoryImplTest {
    @Autowired
    private FacultyRepositoryImpl facultyRepository;
    @Autowired
    private DepartmentRepositoryImpl departmentRepository;

    @Test
    void create_givenValidEntity_thenReturnCreatedFacultyWithId() {
        // Given
        FacultyEntity facultyEntity = faculty("Applied Mathematics", "ami.lnu.edu.ua", "ami@lnu.edu.ua",
                "274-01-80", "1 Universytetska St, Lviv, 79000", "Best faculty at LNU!");

        FacultyEntity createdFacultyEntity = facultyRepository.create(facultyEntity);

        DepartmentEntity entity = department("Applied Mathematics", createdFacultyEntity.getId(), "ami@lnu.edu.ua",
        "274-01-80", "Best faculty at LNU!");

        // When
        var actual = departmentRepository.create(entity);

        // Then
        assertNotNull(actual.getId());

        DepartmentEntity expected = department("Applied Mathematics", createdFacultyEntity.getId(), "ami@lnu.edu.ua",
                "274-01-80", "Best faculty at LNU!");
        expected.setId(actual.getId());

        assertEquals(expected, actual);
    }

    private FacultyEntity faculty(String name, String website, String email,
                                  String phone, String address, String info) {
        FacultyEntity entity = new FacultyEntity();
        entity.setName(name);
        entity.setWebsite(website);
        entity.setEmail(email);
        entity.setPhone(phone);
        entity.setAddress(address);
        entity.setInfo(info);
        return entity;
    }

    private DepartmentEntity department(String name, Long facultyId, String email,
                                            String phone, String info) {
        DepartmentEntity entity = new DepartmentEntity();
        entity.setName(name);
        entity.setFacultyId(facultyId);
        entity.setEmail(email);
        entity.setPhone(phone);
        entity.setInfo(info);
        return entity;
    }
}
