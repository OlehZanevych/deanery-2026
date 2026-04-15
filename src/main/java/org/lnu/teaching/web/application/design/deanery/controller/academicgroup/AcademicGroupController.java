package org.lnu.teaching.web.application.design.deanery.controller.academicgroup;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.lnu.teaching.web.application.design.deanery.annotation.Auth;
import org.lnu.teaching.web.application.design.deanery.dto.academicgroup.AcademicGroupDto;
import org.lnu.teaching.web.application.design.deanery.dto.academicgroup.BaseAcademicGroupDto;
import org.lnu.teaching.web.application.design.deanery.dto.common.ValueDto;
import org.lnu.teaching.web.application.design.deanery.service.academicgroup.AcademicGroupService;
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
@RequestMapping("academic-groups")
@Tag(name = "Academic Groups", description = "Student group management — Spring Data JPA compound queries demo")
public class AcademicGroupController {

    private final AcademicGroupService academicGroupService;

    @Auth(isAdmin = true)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AcademicGroupDto create(@RequestBody BaseAcademicGroupDto dto) {
        return academicGroupService.create(dto);
    }

    @GetMapping
    public List<AcademicGroupDto> findAll() {
        return academicGroupService.findAll();
    }

    @GetMapping("by-curriculum/{curriculumId}")
    @Operation(summary = "Get groups for a curriculum (derived query)")
    public List<AcademicGroupDto> findByCurriculum(@PathVariable Long curriculumId) {
        return academicGroupService.findByCurriculumId(curriculumId);
    }

    @GetMapping("by-enrollment-year/{year}")
    @Operation(summary = "Get groups enrolled in a specific year (derived query)")
    public List<AcademicGroupDto> findByEnrollmentYear(@PathVariable Integer year) {
        return academicGroupService.findByEnrollmentYear(year);
    }

    @GetMapping("enrolled-from")
    @Operation(summary = "Get groups enrolled from a given year onwards (HQL ORDER BY)")
    public List<AcademicGroupDto> findEnrolledFromYear(@RequestParam Integer fromYear) {
        return academicGroupService.findEnrolledFromYear(fromYear);
    }

    @GetMapping("count/by-curriculum/{curriculumId}")
    @Operation(summary = "Count groups in a curriculum")
    public ValueDto<Long> countByCurriculum(@PathVariable Long curriculumId) {
        return academicGroupService.countByCurriculumId(curriculumId);
    }

    @GetMapping("{id}")
    public AcademicGroupDto find(@PathVariable Long id) {
        return academicGroupService.find(id);
    }

    @Auth(isAdmin = true)
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody BaseAcademicGroupDto dto) {
        academicGroupService.update(id, dto);
    }

    @Auth(isAdmin = true)
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an academic group (HQL @Modifying DELETE)")
    public void delete(@PathVariable Long id) {
        academicGroupService.delete(id);
    }
}
