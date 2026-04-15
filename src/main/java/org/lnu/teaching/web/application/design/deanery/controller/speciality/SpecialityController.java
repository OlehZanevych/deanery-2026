package org.lnu.teaching.web.application.design.deanery.controller.speciality;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.lnu.teaching.web.application.design.deanery.annotation.Auth;
import org.lnu.teaching.web.application.design.deanery.dto.common.ValueDto;
import org.lnu.teaching.web.application.design.deanery.dto.speciality.BaseSpecialityDto;
import org.lnu.teaching.web.application.design.deanery.dto.speciality.SpecialityDto;
import org.lnu.teaching.web.application.design.deanery.service.speciality.SpecialityService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Auth
@RestController
@RequiredArgsConstructor
@RequestMapping("specialities")
@Tag(name = "Specialities", description = "Academic speciality management — Spring Data JPA demo")
public class SpecialityController {

    private final SpecialityService specialityService;

    @Auth(isAdmin = true)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new speciality")
    public SpecialityDto create(@RequestBody BaseSpecialityDto dto) {
        return specialityService.create(dto);
    }

    @GetMapping
    @Operation(summary = "List all specialities")
    public List<SpecialityDto> findAll() {
        return specialityService.findAll();
    }

    @GetMapping("search")
    @Operation(summary = "Search specialities by name (case-insensitive HQL LIKE)")
    public List<SpecialityDto> search(@RequestParam String name) {
        return specialityService.search(name);
    }

    @GetMapping("by-department/{departmentId}")
    @Operation(summary = "Get specialities for a department (derived query)")
    public List<SpecialityDto> findByDepartment(@PathVariable Long departmentId) {
        return specialityService.findByDepartmentId(departmentId);
    }

    @GetMapping("by-faculty/{facultyId}")
    @Operation(summary = "Get specialities for a faculty (multi-level HQL path navigation)")
    public List<SpecialityDto> findByFaculty(@PathVariable Long facultyId) {
        return specialityService.findByFacultyId(facultyId);
    }

    @GetMapping("count/by-department/{departmentId}")
    @Operation(summary = "Count specialities in a department (HQL COUNT aggregate)")
    public ValueDto<Long> countByDepartment(@PathVariable Long departmentId) {
        return specialityService.countByDepartmentId(departmentId);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get a speciality by ID")
    public SpecialityDto find(@PathVariable Long id) {
        return specialityService.find(id);
    }

    @Auth(isAdmin = true)
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Replace a speciality")
    public void update(@PathVariable Long id, @RequestBody BaseSpecialityDto dto) {
        specialityService.update(id, dto);
    }

    @Auth(isAdmin = true)
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a speciality by ID (HQL @Modifying DELETE)")
    public void delete(@PathVariable Long id) {
        specialityService.delete(id);
    }
}
