package org.lnu.teaching.web.application.design.deanery.controller.lecturer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.lnu.teaching.web.application.design.deanery.annotation.Auth;
import org.lnu.teaching.web.application.design.deanery.dto.lecturer.BaseLecturerDto;
import org.lnu.teaching.web.application.design.deanery.dto.lecturer.LecturerDto;
import org.lnu.teaching.web.application.design.deanery.service.lecturer.LecturerService;
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
@RequestMapping("lecturers")
@Tag(name = "Lecturers", description = "Lecturer management — Spring Data JPA JOIN FETCH and full-name HQL search demo")
public class LecturerController {

    private final LecturerService lecturerService;

    @Auth(isAdmin = true)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LecturerDto create(@RequestBody BaseLecturerDto dto) {
        return lecturerService.create(dto);
    }

    @GetMapping
    public List<LecturerDto> findAll() {
        return lecturerService.findAll();
    }

    @GetMapping("search")
    @Operation(summary = "Full-name search (HQL CONCAT + LOWER + LIKE)")
    public List<LecturerDto> search(@RequestParam String name) {
        return lecturerService.search(name);
    }

    @GetMapping("by-department/{departmentId}")
    @Operation(summary = "Get lecturers for a department, sorted by last name (derived query with ORDER BY)")
    public List<LecturerDto> findByDepartment(@PathVariable Long departmentId) {
        return lecturerService.findByDepartmentId(departmentId);
    }

    @GetMapping("by-faculty/{facultyId}")
    @Operation(summary = "Get lecturers for a faculty (HQL multi-level path navigation)")
    public List<LecturerDto> findByFaculty(@PathVariable Long facultyId) {
        return lecturerService.findByFacultyId(facultyId);
    }

    @GetMapping("{id}")
    public LecturerDto find(@PathVariable Long id) {
        return lecturerService.find(id);
    }

    @Auth(isAdmin = true)
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody BaseLecturerDto dto) {
        lecturerService.update(id, dto);
    }

    @Auth(isAdmin = true)
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a lecturer (HQL @Modifying DELETE)")
    public void delete(@PathVariable Long id) {
        lecturerService.delete(id);
    }
}
