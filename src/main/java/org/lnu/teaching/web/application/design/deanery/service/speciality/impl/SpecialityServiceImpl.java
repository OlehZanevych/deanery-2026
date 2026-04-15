package org.lnu.teaching.web.application.design.deanery.service.speciality.impl;

import lombok.RequiredArgsConstructor;
import org.lnu.teaching.web.application.design.deanery.dto.common.ValueDto;
import org.lnu.teaching.web.application.design.deanery.dto.speciality.BaseSpecialityDto;
import org.lnu.teaching.web.application.design.deanery.dto.speciality.SpecialityDto;
import org.lnu.teaching.web.application.design.deanery.entity.department.DepartmentEntity;
import org.lnu.teaching.web.application.design.deanery.entity.speciality.SpecialityEntity;
import org.lnu.teaching.web.application.design.deanery.exception.DataConflictException;
import org.lnu.teaching.web.application.design.deanery.exception.NotFoundException;
import org.lnu.teaching.web.application.design.deanery.mapper.SpecialityMapper;
import org.lnu.teaching.web.application.design.deanery.repository.department.DepartmentJpaRepository;
import org.lnu.teaching.web.application.design.deanery.repository.speciality.SpecialityRepository;
import org.lnu.teaching.web.application.design.deanery.service.speciality.SpecialityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialityServiceImpl implements SpecialityService {

    private final SpecialityRepository specialityRepository;
    private final DepartmentJpaRepository departmentRepository;
    private final SpecialityMapper specialityMapper;

    @Override
    @Transactional
    public SpecialityDto create(BaseSpecialityDto dto) {
        if (specialityRepository.existsByCode(dto.getCode())) {
            throw new DataConflictException("Speciality with code \"" + dto.getCode() + "\" already exists!");
        }
        if (specialityRepository.existsByName(dto.getName())) {
            throw new DataConflictException("Speciality with name \"" + dto.getName() + "\" already exists!");
        }

        // Validates that the department exists; throws NotFoundException if not
        DepartmentEntity department = resolveDepartment(dto.getDepartmentId());

        SpecialityEntity entity = specialityMapper.toEntity(dto);
        entity.setDepartment(department);

        return specialityMapper.toDto(specialityRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpecialityDto> findAll() {
        return specialityMapper.toDtoList(specialityRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpecialityDto> findByDepartmentId(Long departmentId) {
        // Uses the derived query method: findByDepartmentId(Long)
        return specialityMapper.toDtoList(specialityRepository.findByDepartmentId(departmentId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpecialityDto> findByFacultyId(Long facultyId) {
        // Uses the HQL query that traverses speciality → department → faculty
        return specialityMapper.toDtoList(specialityRepository.findByFacultyId(facultyId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpecialityDto> search(String name) {
        return specialityMapper.toDtoList(specialityRepository.searchByName(name));
    }

    @Override
    @Transactional(readOnly = true)
    public ValueDto<Long> countByDepartmentId(Long departmentId) {
        return new ValueDto<>(specialityRepository.countByDepartmentId(departmentId));
    }

    @Override
    @Transactional(readOnly = true)
    public SpecialityDto find(Long id) {
        return specialityMapper.toDto(loadSpeciality(id));
    }

    @Override
    @Transactional
    public SpecialityDto update(Long id, BaseSpecialityDto dto) {
        SpecialityEntity entity = loadSpeciality(id);
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setDepartment(resolveDepartment(dto.getDepartmentId()));
        entity.setInfo(dto.getInfo());
        // save() triggers an UPDATE; @DynamicUpdate ensures only changed columns are sent
        return specialityMapper.toDto(specialityRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // Uses the @Modifying HQL rather than the standard deleteById
        int affected = specialityRepository.removeById(id);
        if (affected == 0) {
            throw new NotFoundException("Speciality with id " + id + " not found!");
        }
    }

    private SpecialityEntity loadSpeciality(Long id) {
        return specialityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Speciality with id " + id + " not found!"));
    }

    private DepartmentEntity resolveDepartment(Long departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new NotFoundException("Department with id " + departmentId + " not found!");
        }
        return departmentRepository.getReferenceById(departmentId);
    }
}
