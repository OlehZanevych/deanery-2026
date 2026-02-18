package org.lnu.teaching.web.application.design.deanery.controller.faculty;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.lnu.teaching.web.application.design.deanery.dto.common.ValueDto;
import org.lnu.teaching.web.application.design.deanery.dto.faculty.BaseFacultyDto;
import org.lnu.teaching.web.application.design.deanery.dto.faculty.FacultyDto;
import org.lnu.teaching.web.application.design.deanery.dto.faculty.FacultyPatch;
import org.lnu.teaching.web.application.design.deanery.dto.faculty.query.params.FacultyFilterOptions;
import org.lnu.teaching.web.application.design.deanery.service.faculty.FacultyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("faculties")
public class FacultyController {
    private final FacultyService facultyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FacultyDto create(@RequestBody BaseFacultyDto faculty) {
        return facultyService.create(faculty);
    }

    @GetMapping
    @Operation(
            parameters = {
                    @Parameter(name = "name"),
                    @Parameter(name = "info")
            }
    )
    public List<FacultyDto> findAll(@Parameter(hidden = true) FacultyFilterOptions filterOptions,
                                    @Parameter(name = "limit") @RequestParam(required = false) Integer limit,
                                    @Parameter(name = "offset") @RequestParam(required = false) Integer offset)  {
        return facultyService.findAll(filterOptions, limit, offset);
    }

    @GetMapping("count")
    @Operation(
            parameters = {
                    @Parameter(name = "name"),
                    @Parameter(name = "info")
            }
    )
    public ValueDto<Integer> count(@Parameter(hidden = true) FacultyFilterOptions filterOptions) {
        return facultyService.count(filterOptions);
    }

    @GetMapping("{id}")
    public FacultyDto find(@PathVariable Long id) {
        return facultyService.find(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody BaseFacultyDto facultyDto) {
        facultyService.update(id, facultyDto);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody FacultyPatch facultyPatch) {
        facultyService.patch(id, facultyPatch);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        facultyService.delete(id);
    }
}
