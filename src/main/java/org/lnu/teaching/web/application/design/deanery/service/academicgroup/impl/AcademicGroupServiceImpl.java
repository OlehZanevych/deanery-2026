package org.lnu.teaching.web.application.design.deanery.service.academicgroup.impl;

import lombok.RequiredArgsConstructor;
import org.lnu.teaching.web.application.design.deanery.dto.academicgroup.AcademicGroupDto;
import org.lnu.teaching.web.application.design.deanery.dto.academicgroup.BaseAcademicGroupDto;
import org.lnu.teaching.web.application.design.deanery.dto.common.ValueDto;
import org.lnu.teaching.web.application.design.deanery.entity.academicgroup.AcademicGroupEntity;
import org.lnu.teaching.web.application.design.deanery.entity.curriculum.CurriculumEntity;
import org.lnu.teaching.web.application.design.deanery.exception.DataConflictException;
import org.lnu.teaching.web.application.design.deanery.exception.NotFoundException;
import org.lnu.teaching.web.application.design.deanery.mapper.AcademicGroupMapper;
import org.lnu.teaching.web.application.design.deanery.repository.academicgroup.AcademicGroupRepository;
import org.lnu.teaching.web.application.design.deanery.repository.curriculum.CurriculumRepository;
import org.lnu.teaching.web.application.design.deanery.service.academicgroup.AcademicGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicGroupServiceImpl implements AcademicGroupService {

    private final AcademicGroupRepository academicGroupRepository;
    private final CurriculumRepository curriculumRepository;
    private final AcademicGroupMapper academicGroupMapper;

    @Override
    @Transactional
    public AcademicGroupDto create(BaseAcademicGroupDto dto) {
        if (academicGroupRepository.existsByName(dto.getName())) {
            throw new DataConflictException("Academic group \"" + dto.getName() + "\" already exists!");
        }

        CurriculumEntity curriculum = resolveCurriculum(dto.getCurriculumId());

        AcademicGroupEntity entity = academicGroupMapper.toEntity(dto);
        entity.setCurriculum(curriculum);

        return academicGroupMapper.toDto(academicGroupRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicGroupDto> findAll() {
        return academicGroupMapper.toDtoList(academicGroupRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicGroupDto> findByCurriculumId(Long curriculumId) {
        return academicGroupMapper.toDtoList(academicGroupRepository.findByCurriculumId(curriculumId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicGroupDto> findByEnrollmentYear(Integer year) {
        return academicGroupMapper.toDtoList(academicGroupRepository.findByEnrollmentYear(year));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicGroupDto> findEnrolledFromYear(Integer fromYear) {
        // Uses the HQL @Query "findEnrolledFromYear"
        return academicGroupMapper.toDtoList(academicGroupRepository.findEnrolledFromYear(fromYear));
    }

    @Override
    @Transactional(readOnly = true)
    public ValueDto<Long> countByCurriculumId(Long curriculumId) {
        return new ValueDto<>(academicGroupRepository.countByCurriculumId(curriculumId));
    }

    @Override
    @Transactional(readOnly = true)
    public AcademicGroupDto find(Long id) {
        return academicGroupMapper.toDto(loadGroup(id));
    }

    @Override
    @Transactional
    public AcademicGroupDto update(Long id, BaseAcademicGroupDto dto) {
        AcademicGroupEntity entity = loadGroup(id);
        entity.setName(dto.getName());
        entity.setCurriculum(resolveCurriculum(dto.getCurriculumId()));
        entity.setEnrollmentYear(dto.getEnrollmentYear());
        entity.setInfo(dto.getInfo());
        return academicGroupMapper.toDto(academicGroupRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        int affected = academicGroupRepository.removeById(id);
        if (affected == 0) {
            throw new NotFoundException("Academic group with id " + id + " not found!");
        }
    }

    private AcademicGroupEntity loadGroup(Long id) {
        return academicGroupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Academic group with id " + id + " not found!"));
    }

    private CurriculumEntity resolveCurriculum(Long curriculumId) {
        if (!curriculumRepository.existsById(curriculumId)) {
            throw new NotFoundException("Curriculum with id " + curriculumId + " not found!");
        }
        return curriculumRepository.getReferenceById(curriculumId);
    }
}
