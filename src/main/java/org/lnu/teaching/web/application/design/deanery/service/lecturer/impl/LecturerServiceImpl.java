package org.lnu.teaching.web.application.design.deanery.service.lecturer.impl;

import lombok.RequiredArgsConstructor;
import org.lnu.teaching.web.application.design.deanery.dto.lecturer.BaseLecturerDto;
import org.lnu.teaching.web.application.design.deanery.dto.lecturer.LecturerDto;
import org.lnu.teaching.web.application.design.deanery.entity.department.DepartmentEntity;
import org.lnu.teaching.web.application.design.deanery.entity.lecturer.LecturerEntity;
import org.lnu.teaching.web.application.design.deanery.exception.DataConflictException;
import org.lnu.teaching.web.application.design.deanery.exception.NotFoundException;
import org.lnu.teaching.web.application.design.deanery.mapper.LecturerMapper;
import org.lnu.teaching.web.application.design.deanery.repository.department.DepartmentJpaRepository;
import org.lnu.teaching.web.application.design.deanery.repository.lecturer.LecturerRepository;
import org.lnu.teaching.web.application.design.deanery.service.lecturer.LecturerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LecturerServiceImpl implements LecturerService {

    private final LecturerRepository lecturerRepository;
    private final DepartmentJpaRepository departmentRepository;
    private final LecturerMapper lecturerMapper;

    @Override
    @Transactional
    public LecturerDto create(BaseLecturerDto dto) {
        if (lecturerRepository.existsByEmail(dto.getEmail())) {
            throw new DataConflictException("Lecturer with email \"" + dto.getEmail() + "\" already exists!");
        }

        DepartmentEntity department = resolveDepartment(dto.getDepartmentId());

        LecturerEntity entity = lecturerMapper.toEntity(dto);
        entity.setDepartment(department);

        return lecturerMapper.toDto(lecturerRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LecturerDto> findAll() {
        return lecturerMapper.toDtoList(lecturerRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LecturerDto> findByDepartmentId(Long departmentId) {
        // Uses the derived query with ORDER BY: findByDepartmentIdOrderByLastNameAsc
        return lecturerMapper.toDtoList(
                lecturerRepository.findByDepartmentIdOrderByLastNameAsc(departmentId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LecturerDto> findByFacultyId(Long facultyId) {
        return lecturerMapper.toDtoList(lecturerRepository.findByFacultyId(facultyId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LecturerDto> search(String name) {
        // Uses the HQL CONCAT / LOWER full-name search
        return lecturerMapper.toDtoList(lecturerRepository.searchByFullName(name));
    }

    @Override
    @Transactional(readOnly = true)
    public LecturerDto find(Long id) {
        return lecturerMapper.toDto(loadLecturer(id));
    }

    @Override
    @Transactional
    public LecturerDto update(Long id, BaseLecturerDto dto) {
        LecturerEntity entity = loadLecturer(id);
        entity.setFirstName(dto.getFirstName());
        entity.setMiddleName(dto.getMiddleName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setTitle(dto.getTitle());
        entity.setDepartment(resolveDepartment(dto.getDepartmentId()));
        entity.setInfo(dto.getInfo());
        return lecturerMapper.toDto(lecturerRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        int affected = lecturerRepository.removeById(id);
        if (affected == 0) {
            throw new NotFoundException("Lecturer with id " + id + " not found!");
        }
    }

    private LecturerEntity loadLecturer(Long id) {
        return lecturerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Lecturer with id " + id + " not found!"));
    }

    private DepartmentEntity resolveDepartment(Long departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new NotFoundException("Department with id " + departmentId + " not found!");
        }
        return departmentRepository.getReferenceById(departmentId);
    }
}
