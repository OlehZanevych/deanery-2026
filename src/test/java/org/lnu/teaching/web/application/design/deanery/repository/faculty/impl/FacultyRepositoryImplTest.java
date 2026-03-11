package org.lnu.teaching.web.application.design.deanery.repository.faculty.impl;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseType;
import org.junit.jupiter.api.Test;
import org.lnu.teaching.web.application.design.deanery.dto.faculty.FacultyPatch;
import org.lnu.teaching.web.application.design.deanery.dto.faculty.query.params.FacultyFilterOptions;
import org.lnu.teaching.web.application.design.deanery.entity.faculty.FacultyEntity;
import org.lnu.teaching.web.application.design.deanery.exception.DataConflictException;
import org.lnu.teaching.web.application.design.deanery.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Sql("/db/schema.sql")
@AutoConfigureEmbeddedDatabase(type = DatabaseType.POSTGRES)
class FacultyRepositoryImplTest {

    @Autowired
    private FacultyRepositoryImpl facultyRepository;

    @Test
    void create_givenValidEntity_thenReturnCreatedFacultyWithId() {
        // Given
        FacultyEntity entity = faculty("Applied Mathematics", "ami.lnu.edu.ua", "ami@lnu.edu.ua",
                "274-01-80", "1 Universytetska St, Lviv, 79000", "Best faculty at LNU!");

        // When
        FacultyEntity actual = facultyRepository.create(entity);

        // Then
        assertNotNull(actual.getId());

        FacultyEntity expected = faculty("Applied Mathematics", "ami.lnu.edu.ua", "ami@lnu.edu.ua",
                "274-01-80", "1 Universytetska St, Lviv, 79000", "Best faculty at LNU!");
        expected.setId(actual.getId());

        assertEquals(expected, actual);
    }

    @Test
    void create_givenDuplicateName_thenThrowDataConflictException() {
        // Given
        FacultyEntity first = faculty("Physics", "phys.lnu.edu.ua", "phys@lnu.edu.ua",
                "111-11-11", "1 Universytetska St, Lviv, 79000", null);
        facultyRepository.create(first);

        FacultyEntity duplicate = faculty("Physics", "other.lnu.edu.ua", "other@lnu.edu.ua",
                "222-22-22", "2 Universytetska St, Lviv, 79000", null);

        // When / Then
        assertThrows(DataConflictException.class, () -> facultyRepository.create(duplicate));
    }

    @Test
    void findAll_givenNoFilters_thenReturnAllFaculties() {
        // Given
        facultyRepository.create(faculty("Biology", "bio.lnu.edu.ua", "bio@lnu.edu.ua",
                "333-33-33", "1 Universytetska St, Lviv, 79000", null));
        facultyRepository.create(faculty("Chemistry", "chem.lnu.edu.ua", "chem@lnu.edu.ua",
                "444-44-44", "2 Universytetska St, Lviv, 79000", null));

        FacultyFilterOptions noFilters = new FacultyFilterOptions();

        // When
        List<FacultyEntity> result = facultyRepository.findAll(noFilters, null, null);

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void findAll_givenNameFilter_thenReturnMatchingFaculties() {
        // Given
        facultyRepository.create(faculty("Geography", "geo.lnu.edu.ua", "geo@lnu.edu.ua",
                "555-55-55", "1 Universytetska St, Lviv, 79000", null));
        facultyRepository.create(faculty("Geology", "geol.lnu.edu.ua", "geol@lnu.edu.ua",
                "666-66-66", "2 Universytetska St, Lviv, 79000", null));
        facultyRepository.create(faculty("History", "hist.lnu.edu.ua", "hist@lnu.edu.ua",
                "777-77-77", "3 Universytetska St, Lviv, 79000", null));

        FacultyFilterOptions filter = new FacultyFilterOptions();
        filter.setName("Geo");

        // When
        List<FacultyEntity> result = facultyRepository.findAll(filter, null, null);

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void findAll_givenLimitAndOffset_thenReturnPagedFaculties() {
        // Given
        facultyRepository.create(faculty("Faculty A", "a.lnu.edu.ua", "a@lnu.edu.ua",
                "100-00-01", "1 Universytetska St, Lviv, 79000", null));
        facultyRepository.create(faculty("Faculty B", "b.lnu.edu.ua", "b@lnu.edu.ua",
                "100-00-02", "2 Universytetska St, Lviv, 79000", null));
        facultyRepository.create(faculty("Faculty C", "c.lnu.edu.ua", "c@lnu.edu.ua",
                "100-00-03", "3 Universytetska St, Lviv, 79000", null));

        FacultyFilterOptions noFilters = new FacultyFilterOptions();

        // When
        List<FacultyEntity> result = facultyRepository.findAll(noFilters, 2, 1);

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void count_givenNoFilters_thenReturnTotalCount() {
        // Given
        facultyRepository.create(faculty("Law", "law.lnu.edu.ua", "law@lnu.edu.ua",
                "888-88-88", "1 Universytetska St, Lviv, 79000", null));
        facultyRepository.create(faculty("Economics", "econ.lnu.edu.ua", "econ@lnu.edu.ua",
                "999-99-99", "2 Universytetska St, Lviv, 79000", null));

        FacultyFilterOptions noFilters = new FacultyFilterOptions();

        // When
        int count = facultyRepository.count(noFilters);

        // Then
        assertEquals(2, count);
    }

    @Test
    void count_givenInfoFilter_thenReturnMatchingCount() {
        // Given
        facultyRepository.create(faculty("Journalism", "jour.lnu.edu.ua", "jour@lnu.edu.ua",
                "101-01-01", "1 Universytetska St, Lviv, 79000", "Media studies"));
        facultyRepository.create(faculty("Philosophy", "phil.lnu.edu.ua", "phil@lnu.edu.ua",
                "102-02-02", "2 Universytetska St, Lviv, 79000", "Ancient wisdom"));

        FacultyFilterOptions filter = new FacultyFilterOptions();
        filter.setInfo("Media");

        // When
        int count = facultyRepository.count(filter);

        // Then
        assertEquals(1, count);
    }

    @Test
    void find_givenExistingId_thenReturnFaculty() {
        // Given
        FacultyEntity created = facultyRepository.create(faculty("Sociology", "soc.lnu.edu.ua",
                "soc@lnu.edu.ua", "103-03-03", "1 Universytetska St, Lviv, 79000", null));

        // When
        FacultyEntity actual = facultyRepository.find(created.getId());

        // Then
        assertEquals(created, actual);
    }

    @Test
    void find_givenNonExistingId_thenThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> facultyRepository.find(-1L));
    }

    @Test
    void update_givenExistingEntity_thenEntityIsUpdated() {
        // Given
        FacultyEntity created = facultyRepository.create(faculty("Psychology", "psy.lnu.edu.ua",
                "psy@lnu.edu.ua", "104-04-04", "1 Universytetska St, Lviv, 79000", null));

        created.setName("Psychology Updated");
        created.setWebsite("psy2.lnu.edu.ua");

        // When
        facultyRepository.update(created);

        // Then
        FacultyEntity actual = facultyRepository.find(created.getId());
        assertEquals(created, actual);
    }

    @Test
    void update_givenNonExistingId_thenThrowNotFoundException() {
        // Given
        FacultyEntity entity = faculty("Ghost Faculty", "ghost.lnu.edu.ua", "ghost@lnu.edu.ua",
                "000-00-00", "Nowhere St", null);
        entity.setId(-1L);

        // When / Then
        assertThrows(NotFoundException.class, () -> facultyRepository.update(entity));
    }

    @Test
    void update_givenDuplicateName_thenThrowDataConflictException() {
        // Given
        facultyRepository.create(faculty("Architecture", "arch.lnu.edu.ua", "arch@lnu.edu.ua",
                "105-05-05", "1 Universytetska St, Lviv, 79000", null));
        FacultyEntity second = facultyRepository.create(faculty("Arts", "arts.lnu.edu.ua",
                "arts@lnu.edu.ua", "106-06-06", "2 Universytetska St, Lviv, 79000", null));

        second.setName("Architecture");

        // When / Then
        assertThrows(DataConflictException.class, () -> facultyRepository.update(second));
    }

    @Test
    void patch_givenExistingId_thenEntityIsPatched() {
        // Given
        FacultyEntity created = facultyRepository.create(faculty("Medicine", "med.lnu.edu.ua",
                "med@lnu.edu.ua", "107-07-07", "1 Universytetska St, Lviv, 79000", null));

        FacultyPatch patch = new FacultyPatch();
        patch.setName("Medicine Updated");

        // When
        facultyRepository.patch(created.getId(), patch);

        // Then
        FacultyEntity actual = facultyRepository.find(created.getId());
        assertEquals("Medicine Updated", actual.getName());
    }

    @Test
    void patch_givenNonExistingId_thenThrowNotFoundException() {
        // Given
        FacultyPatch patch = new FacultyPatch();
        patch.setName("Ghost");

        // When / Then
        assertThrows(NotFoundException.class, () -> facultyRepository.patch(-1L, patch));
    }

    @Test
    void patch_givenDuplicateName_thenThrowDataConflictException() {
        // Given
        facultyRepository.create(faculty("Dentistry", "dent.lnu.edu.ua", "dent@lnu.edu.ua",
                "108-08-08", "1 Universytetska St, Lviv, 79000", null));
        FacultyEntity second = facultyRepository.create(faculty("Pharmacy", "pharm.lnu.edu.ua",
                "pharm@lnu.edu.ua", "109-09-09", "2 Universytetska St, Lviv, 79000", null));

        FacultyPatch patch = new FacultyPatch();
        patch.setName("Dentistry");

        // When / Then
        assertThrows(DataConflictException.class, () -> facultyRepository.patch(second.getId(), patch));
    }

    @Test
    void delete_givenExistingId_thenFacultyIsDeleted() {
        // Given
        FacultyEntity created = facultyRepository.create(faculty("Veterinary", "vet.lnu.edu.ua",
                "vet@lnu.edu.ua", "110-10-10", "1 Universytetska St, Lviv, 79000", null));

        // When
        facultyRepository.delete(created.getId());

        // Then
        assertThrows(NotFoundException.class, () -> facultyRepository.find(created.getId()));
    }

    @Test
    void delete_givenNonExistingId_thenThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> facultyRepository.delete(-1L));
    }

    @Test
    void findAll_givenZeroOffset_thenOffsetIsIgnored() {
        // Given
        facultyRepository.create(faculty("Linguistics", "ling.lnu.edu.ua", "ling@lnu.edu.ua",
                "111-22-33", "1 Universytetska St, Lviv, 79000", null));
        facultyRepository.create(faculty("Mathematics", "math.lnu.edu.ua", "math@lnu.edu.ua",
                "111-22-44", "2 Universytetska St, Lviv, 79000", null));

        FacultyFilterOptions noFilters = new FacultyFilterOptions();

        // When
        List<FacultyEntity> result = facultyRepository.findAll(noFilters, null, 0);

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void patch_givenAllFields_thenAllFieldsArePatched() {
        // Given
        FacultyEntity created = facultyRepository.create(faculty("Cybernetics", "cyber.lnu.edu.ua",
                "cyber@lnu.edu.ua", "200-00-01", "1 Universytetska St, Lviv, 79000", "Old info"));

        FacultyPatch patch = new FacultyPatch();
        patch.setName("Cybernetics Updated");
        patch.setWebsite("cyber2.lnu.edu.ua");
        patch.setEmail("cyber2@lnu.edu.ua");
        patch.setPhone("200-00-02");
        patch.setAddress("2 Universytetska St, Lviv, 79000");
        patch.setInfo("New info");

        // When
        facultyRepository.patch(created.getId(), patch);

        // Then
        FacultyEntity actual = facultyRepository.find(created.getId());
        assertEquals("Cybernetics Updated", actual.getName());
        assertEquals("cyber2.lnu.edu.ua", actual.getWebsite());
        assertEquals("cyber2@lnu.edu.ua", actual.getEmail());
        assertEquals("200-00-02", actual.getPhone());
        assertEquals("2 Universytetska St, Lviv, 79000", actual.getAddress());
        assertEquals("New info", actual.getInfo());
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
}
