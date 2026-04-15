package org.lnu.teaching.web.application.design.deanery.controller.curriculum;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.lnu.teaching.web.application.design.deanery.annotation.Auth;
import org.lnu.teaching.web.application.design.deanery.dto.curriculum.BaseCurriculumDto;
import org.lnu.teaching.web.application.design.deanery.dto.curriculum.CurriculumDto;
import org.lnu.teaching.web.application.design.deanery.service.curriculum.CurriculumService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("curricula")
@Tag(name = "Curricula", description = "Academic curriculum management — Spring Data JPA pagination demo")
public class CurriculumController {

    private final CurriculumService curriculumService;

    @Auth(isAdmin = true)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CurriculumDto create(@RequestBody BaseCurriculumDto dto) {
        return curriculumService.create(dto);
    }

    @GetMapping
    @Operation(summary = "List all curricula")
    public List<CurriculumDto> findAll() {
        return curriculumService.findAll();
    }

    @GetMapping("search")
    @Operation(summary = "Search curricula by name (HQL LIKE)")
    public List<CurriculumDto> search(@RequestParam String name) {
        return curriculumService.search(name);
    }

    @GetMapping("by-degree")
    @Operation(summary = "Filter curricula by degree level (bachelor / master / phd)")
    public List<CurriculumDto> findByDegree(@RequestParam String degree) {
        return curriculumService.findByDegree(degree);
    }

    @GetMapping("by-speciality/{specialityId}")
    @Operation(summary = "Get paginated curricula for a speciality (Spring Data JPA Pageable)")
    public Page<CurriculumDto> findBySpeciality(
            @PathVariable Long specialityId,
            @PageableDefault(size = 10, sort = "year") Pageable pageable) {
        return curriculumService.findBySpecialityId(specialityId, pageable);
    }

    @GetMapping("by-faculty/{facultyId}")
    @Operation(summary = "Get curricula for a faculty (multi-level HQL navigation)")
    public List<CurriculumDto> findByFaculty(@PathVariable Long facultyId) {
        return curriculumService.findByFacultyId(facultyId);
    }

    @GetMapping("{id}")
    public CurriculumDto find(@PathVariable Long id) {
        return curriculumService.find(id);
    }

    @Auth(isAdmin = true)
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody BaseCurriculumDto dto) {
        curriculumService.update(id, dto);
    }

    @Auth(isAdmin = true)
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a curriculum (HQL @Modifying DELETE)")
    public void delete(@PathVariable Long id) {
        curriculumService.delete(id);
    }
}
