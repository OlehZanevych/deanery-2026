package org.lnu.teaching.web.application.design.deanery.service.faculty.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lnu.teaching.web.application.design.deanery.dto.common.ValueDto;
import org.lnu.teaching.web.application.design.deanery.dto.faculty.BaseFacultyDto;
import org.lnu.teaching.web.application.design.deanery.dto.faculty.FacultyDto;
import org.lnu.teaching.web.application.design.deanery.dto.faculty.FacultyPatch;
import org.lnu.teaching.web.application.design.deanery.dto.faculty.query.params.FacultyFilterOptions;
import org.lnu.teaching.web.application.design.deanery.entity.faculty.FacultyEntity;
import org.lnu.teaching.web.application.design.deanery.exception.BadRequestException;
import org.lnu.teaching.web.application.design.deanery.mapper.FacultyMapper;
import org.lnu.teaching.web.application.design.deanery.repository.faculty.FacultyRepository;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FacultyServiceImplTest {

    @Mock
    private FacultyRepository facultyRepository;

    @Mock
    private FacultyMapper facultyMapper;

    @InjectMocks
    private FacultyServiceImpl facultyService;

    @Test
    void create_givenValidDto_thenReturnCreatedFacultyDto() {
        // Given
        BaseFacultyDto baseFacultyDto = new BaseFacultyDto();
        FacultyEntity mappedEntity = new FacultyEntity();
        FacultyEntity createdEntity = new FacultyEntity();
        FacultyDto expectedDto = new FacultyDto();
        expectedDto.setId(147L);

        when(facultyMapper.toEntity(baseFacultyDto)).thenReturn(mappedEntity);
        when(facultyRepository.create(mappedEntity)).thenReturn(createdEntity);
        when(facultyMapper.toDto(createdEntity)).thenReturn(expectedDto);

        // When
        FacultyDto actualDto = facultyService.create(baseFacultyDto);

        // Then
        assertEquals(expectedDto, actualDto);

        InOrder inOrder = inOrder(facultyRepository, facultyMapper);
        inOrder.verify(facultyMapper).toEntity(baseFacultyDto);
        inOrder.verify(facultyRepository).create(mappedEntity);
        inOrder.verify(facultyMapper).toDto(createdEntity);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void findAll_givenFilterOptions_thenReturnMappedDtoList() {
        // Given
        FacultyFilterOptions filterOptions = new FacultyFilterOptions();
        List<FacultyEntity> entities = List.of(new FacultyEntity());
        List<FacultyDto> expectedDtos = List.of(new FacultyDto());

        when(facultyRepository.findAll(filterOptions, null, null)).thenReturn(entities);
        when(facultyMapper.toDtoList(entities)).thenReturn(expectedDtos);

        // When
        List<FacultyDto> actualDtos = facultyService.findAll(filterOptions, null, null);

        // Then
        assertEquals(expectedDtos, actualDtos);

        InOrder inOrder = inOrder(facultyRepository, facultyMapper);
        inOrder.verify(facultyRepository).findAll(filterOptions, null, null);
        inOrder.verify(facultyMapper).toDtoList(entities);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void count_givenFilterOptions_thenReturnValueDto() {
        // Given
        FacultyFilterOptions filterOptions = new FacultyFilterOptions();

        when(facultyRepository.count(filterOptions)).thenReturn(5);

        // When
        ValueDto<Integer> actual = facultyService.count(filterOptions);

        // Then
        assertEquals(new ValueDto<>(5), actual);

        verify(facultyRepository).count(filterOptions);
        verifyNoMoreInteractions(facultyRepository, facultyMapper);
    }

    @Test
    void find_givenExistingId_thenReturnMappedDto() {
        // Given
        FacultyEntity entity = new FacultyEntity();
        FacultyDto expectedDto = new FacultyDto();
        expectedDto.setId(1L);

        when(facultyRepository.find(1L)).thenReturn(entity);
        when(facultyMapper.toDto(entity)).thenReturn(expectedDto);

        // When
        FacultyDto actualDto = facultyService.find(1L);

        // Then
        assertEquals(expectedDto, actualDto);

        InOrder inOrder = inOrder(facultyRepository, facultyMapper);
        inOrder.verify(facultyRepository).find(1L);
        inOrder.verify(facultyMapper).toDto(entity);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void update_givenValidDto_thenRepositoryUpdateIsCalledWithId() {
        // Given
        BaseFacultyDto baseFacultyDto = new BaseFacultyDto();
        FacultyEntity mappedEntity = new FacultyEntity();

        when(facultyMapper.toEntity(baseFacultyDto)).thenReturn(mappedEntity);

        // When
        facultyService.update(1L, baseFacultyDto);

        // Then
        assertEquals(1L, mappedEntity.getId());

        InOrder inOrder = inOrder(facultyRepository, facultyMapper);
        inOrder.verify(facultyMapper).toEntity(baseFacultyDto);
        inOrder.verify(facultyRepository).update(mappedEntity);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void patch_givenValidPatch_thenRepositoryPatchIsCalled() {
        // Given
        FacultyPatch facultyPatch = new FacultyPatch();
        facultyPatch.setName("Updated Name");

        // When
        facultyService.patch(1L, facultyPatch);

        // Then
        verify(facultyRepository).patch(1L, facultyPatch);
        verifyNoMoreInteractions(facultyRepository, facultyMapper);
    }

    @Test
    void patch_givenEmptyPatch_thenThrowBadRequestException() {
        // Given
        FacultyPatch emptyPatch = new FacultyPatch();

        // When / Then
        assertThrows(BadRequestException.class, () -> facultyService.patch(1L, emptyPatch));

        verifyNoMoreInteractions(facultyRepository, facultyMapper);
    }

    @Test
    void delete_givenExistingId_thenRepositoryDeleteIsCalled() {
        // When
        facultyService.delete(1L);

        // Then
        verify(facultyRepository).delete(1L);
        verifyNoMoreInteractions(facultyRepository, facultyMapper);
    }
}
