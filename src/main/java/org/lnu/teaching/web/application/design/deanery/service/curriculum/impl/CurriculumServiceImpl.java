package org.lnu.teaching.web.application.design.deanery.service.curriculum.impl;

import lombok.RequiredArgsConstructor;
import org.lnu.teaching.web.application.design.deanery.dto.curriculum.BaseCurriculumDto;
import org.lnu.teaching.web.application.design.deanery.dto.curriculum.CurriculumDto;
import org.lnu.teaching.web.application.design.deanery.entity.curriculum.CurriculumEntity;
import org.lnu.teaching.web.application.design.deanery.entity.speciality.SpecialityEntity;
import org.lnu.teaching.web.application.design.deanery.exception.DataConflictException;
import org.lnu.teaching.web.application.design.deanery.exception.NotFoundException;
import org.lnu.teaching.web.application.design.deanery.mapper.CurriculumMapper;
import org.lnu.teaching.web.application.design.deanery.repository.curriculum.CurriculumRepository;
import org.lnu.teaching.web.application.design.deanery.repository.speciality.SpecialityRepository;
import org.lnu.teaching.web.application.design.deanery.service.curriculum.CurriculumService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurriculumServiceImpl implements CurriculumService {

    private final CurriculumRepository curriculumRepository;
    private final SpecialityRepository specialityRepository;
    private final CurriculumMapper curriculumMapper;

    @Override
    @Transactional
    public CurriculumDto create(BaseCurriculumDto dto) {
        if (curriculumRepository.existsBySpecialityIdAndDegreeAndYear(
                dto.getSpecialityId(), dto.getDegree(), dto.getYear())) {
            throw new DataConflictException(
                    "A curriculum for speciality " + dto.getSpecialityId()
                    + " with degree \"" + dto.getDegree() + "\" and year " + dto.getYear() + " already exists!");
        }

        SpecialityEntity speciality = resolveSpeciality(dto.getSpecialityId());

        CurriculumEntity entity = curriculumMapper.toEntity(dto);
        entity.setSpeciality(speciality);

        return curriculumMapper.toDto(curriculumRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CurriculumDto> findAll() {
        return curriculumMapper.toDtoList(curriculumRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CurriculumDto> findBySpecialityId(Long specialityId, Pageable pageable) {
        // Returns a Page<CurriculumEntity> and maps each element to a DTO
        return curriculumRepository.findBySpecialityId(specialityId, pageable)
                .map(curriculumMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CurriculumDto> findByDegree(String degree) {
        return curriculumMapper.toDtoList(curriculumRepository.findByDegree(degree));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CurriculumDto> findByFacultyId(Long facultyId) {
        return curriculumMapper.toDtoList(curriculumRepository.findByFacultyId(facultyId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CurriculumDto> search(String name) {
        return curriculumMapper.toDtoList(curriculumRepository.searchByName(name));
    }

    @Override
    @Transactional(readOnly = true)
    public CurriculumDto find(Long id) {
        return curriculumMapper.toDto(loadCurriculum(id));
    }

    @Override
    @Transactional
    public CurriculumDto update(Long id, BaseCurriculumDto dto) {
        CurriculumEntity entity = loadCurriculum(id);
        entity.setName(dto.getName());
        entity.setSpeciality(resolveSpeciality(dto.getSpecialityId()));
        entity.setYear(dto.getYear());
        entity.setDegree(dto.getDegree());
        entity.setDurationYears(dto.getDurationYears());
        entity.setInfo(dto.getInfo());
        return curriculumMapper.toDto(curriculumRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        int affected = curriculumRepository.removeById(id);
        if (affected == 0) {
            throw new NotFoundException("Curriculum with id " + id + " not found!");
        }
    }

    private CurriculumEntity loadCurriculum(Long id) {
        return curriculumRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Curriculum with id " + id + " not found!"));
    }

    private SpecialityEntity resolveSpeciality(Long specialityId) {
        if (!specialityRepository.existsById(specialityId)) {
            throw new NotFoundException("Speciality with id " + specialityId + " not found!");
        }
        return specialityRepository.getReferenceById(specialityId);
    }
}
