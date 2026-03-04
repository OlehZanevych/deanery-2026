package org.lnu.teaching.web.application.design.deanery.service.department.impl;

import lombok.AllArgsConstructor;
import org.lnu.teaching.web.application.design.deanery.dto.common.ValueDto;
import org.lnu.teaching.web.application.design.deanery.dto.department.BaseDepartmentDto;
import org.lnu.teaching.web.application.design.deanery.dto.department.DepartmentDto;
import org.lnu.teaching.web.application.design.deanery.dto.department.DepartmentPatch;
import org.lnu.teaching.web.application.design.deanery.dto.department.query.params.DepartmentFilterOptions;
import org.lnu.teaching.web.application.design.deanery.entity.department.DepartmentEntity;
import org.lnu.teaching.web.application.design.deanery.exception.BadRequestException;
import org.lnu.teaching.web.application.design.deanery.mapper.DepartmentMapper;
import org.lnu.teaching.web.application.design.deanery.repository.department.DepartmentRepository;
import org.lnu.teaching.web.application.design.deanery.service.department.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentDto create(BaseDepartmentDto departmentDto) {
        DepartmentEntity entity = departmentMapper.toEntity(departmentDto);
        DepartmentEntity created = departmentRepository.create(entity);
        return departmentMapper.toDto(created);
    }

    @Override
    public List<DepartmentDto> findAll(DepartmentFilterOptions filterOptions, Integer limit, Integer offset) {
        return departmentMapper.toDtoList(departmentRepository.findAll(filterOptions, limit, offset));
    }

    @Override
    public ValueDto<Integer> count(DepartmentFilterOptions filterOptions) {
        return new ValueDto<>(departmentRepository.count(filterOptions));
    }

    @Override
    public DepartmentDto find(Long id) {
        return departmentMapper.toDto(departmentRepository.find(id));
    }

    @Override
    public void update(Long id, BaseDepartmentDto departmentDto) {
        DepartmentEntity entity = departmentMapper.toEntity(departmentDto);
        entity.setId(id);
        departmentRepository.update(entity);
    }

    @Override
    public void patch(Long id, DepartmentPatch departmentPatch) {
        if (departmentPatch.isEmpty()) {
            throw new BadRequestException("Department patch is empty!");
        }
        departmentRepository.patch(id, departmentPatch);
    }

    @Override
    public void delete(Long id) {
        departmentRepository.delete(id);
    }
}
