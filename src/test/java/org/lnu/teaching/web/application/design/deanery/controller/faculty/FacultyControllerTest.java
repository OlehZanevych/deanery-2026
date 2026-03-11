package org.lnu.teaching.web.application.design.deanery.controller.faculty;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lnu.teaching.web.application.design.deanery.dto.common.ValueDto;
import org.lnu.teaching.web.application.design.deanery.dto.faculty.BaseFacultyDto;
import org.lnu.teaching.web.application.design.deanery.dto.faculty.FacultyDto;
import org.lnu.teaching.web.application.design.deanery.dto.faculty.FacultyPatch;
import org.lnu.teaching.web.application.design.deanery.dto.faculty.query.params.FacultyFilterOptions;
import org.lnu.teaching.web.application.design.deanery.service.faculty.FacultyService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FacultyControllerTest {
    @Mock
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(facultyController).build();
    }

    @Test
    void create_givenValidRequest_thenReturnCreatedFaculty() throws Exception {
        // Given
        FacultyDto facultyDto = new FacultyDto();
        facultyDto.setId(147L);
        facultyDto.setName("Applied Mathematics and Informatics");
        facultyDto.setWebsite("ami.lnu.edu.ua");
        facultyDto.setEmail("ami@lnu.edu.ua");
        facultyDto.setPhone("274-01-80");
        facultyDto.setAddress("1 Universytetska St, Lviv, 79000");
        facultyDto.setInfo("Best faculty at LNU!");

        when(facultyService.create(any(BaseFacultyDto.class))).thenReturn(facultyDto);

        String requestBody = "{" +
                "\"name\":\"Applied Mathematics and Informatics\"," +
                "\"website\":\"ami.lnu.edu.ua\"," +
                "\"email\":\"ami@lnu.edu.ua\"," +
                "\"phone\":\"274-01-80\"," +
                "\"address\":\"1 Universytetska St, Lviv, 79000\"," +
                "\"info\":\"Best faculty at LNU!\"" +
                "}";

        // When / Then
        mockMvc.perform(post("/faculties")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(147))
                .andExpect(jsonPath("$.name").value("Applied Mathematics and Informatics"))
                .andExpect(jsonPath("$.website").value("ami.lnu.edu.ua"))
                .andExpect(jsonPath("$.email").value("ami@lnu.edu.ua"))
                .andExpect(jsonPath("$.phone").value("274-01-80"))
                .andExpect(jsonPath("$.address").value("1 Universytetska St, Lviv, 79000"))
                .andExpect(jsonPath("$.info").value("Best faculty at LNU!"));

        verify(facultyService).create(any(BaseFacultyDto.class));
        verifyNoMoreInteractions(facultyService);
    }

    @Test
    void findAll_givenFilterOptions_thenReturnFacultyList() throws Exception {
        // Given
        FacultyDto facultyDto = new FacultyDto();
        facultyDto.setId(1L);
        facultyDto.setName("Applied Mathematics and Informatics");

        when(facultyService.findAll(any(FacultyFilterOptions.class), eq(null), eq(null)))
                .thenReturn(List.of(facultyDto));

        // When / Then
        mockMvc.perform(get("/faculties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Applied Mathematics and Informatics"));

        verify(facultyService).findAll(any(FacultyFilterOptions.class), eq(null), eq(null));
        verifyNoMoreInteractions(facultyService);
    }

    @Test
    void count_givenFilterOptions_thenReturnFacultyCount() throws Exception {
        // Given
        when(facultyService.count(any(FacultyFilterOptions.class)))
                .thenReturn(new ValueDto<>(5));

        // When / Then
        mockMvc.perform(get("/faculties/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(5));

        verify(facultyService).count(any(FacultyFilterOptions.class));
        verifyNoMoreInteractions(facultyService);
    }

    @Test
    void find_givenExistingId_thenReturnFaculty() throws Exception {
        // Given
        FacultyDto facultyDto = new FacultyDto();
        facultyDto.setId(1L);
        facultyDto.setName("Applied Mathematics and Informatics");

        when(facultyService.find(1L)).thenReturn(facultyDto);

        // When / Then
        mockMvc.perform(get("/faculties/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Applied Mathematics and Informatics"));

        verify(facultyService).find(1L);
        verifyNoMoreInteractions(facultyService);
    }

    @Test
    void update_givenValidRequest_thenReturnNoContent() throws Exception {
        // Given
        String requestBody = "{" +
                "\"name\":\"Applied Mathematics and Informatics\"," +
                "\"website\":\"ami.lnu.edu.ua\"," +
                "\"email\":\"ami@lnu.edu.ua\"," +
                "\"phone\":\"274-01-80\"," +
                "\"address\":\"1 Universytetska St, Lviv, 79000\"," +
                "\"info\":\"Best faculty at LNU!\"" +
                "}";

        // When / Then
        mockMvc.perform(put("/faculties/1")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(facultyService).update(eq(1L), any(BaseFacultyDto.class));
        verifyNoMoreInteractions(facultyService);
    }

    @Test
    void patch_givenPartialRequest_thenReturnNoContent() throws Exception {
        // Given
        String requestBody = "{\"name\":\"Updated Faculty Name\"}";

        // When / Then
        mockMvc.perform(patch("/faculties/1")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(facultyService).patch(eq(1L), any(FacultyPatch.class));
        verifyNoMoreInteractions(facultyService);
    }

    @Test
    void delete_givenExistingId_thenReturnNoContent() throws Exception {
        // When / Then
        mockMvc.perform(delete("/faculties/1"))
                .andExpect(status().isNoContent());

        verify(facultyService).delete(1L);
        verifyNoMoreInteractions(facultyService);
    }
}
