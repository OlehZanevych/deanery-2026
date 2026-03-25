package org.lnu.teaching.web.application.design.deanery.controller.department;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.lnu.teaching.web.application.design.deanery.annotation.Auth;
import org.lnu.teaching.web.application.design.deanery.dto.common.ValueDto;
import org.lnu.teaching.web.application.design.deanery.dto.department.BaseDepartmentDto;
import org.lnu.teaching.web.application.design.deanery.dto.department.DepartmentDto;
import org.lnu.teaching.web.application.design.deanery.dto.department.DepartmentPatch;
import org.lnu.teaching.web.application.design.deanery.dto.department.query.params.DepartmentFilterOptions;
import org.lnu.teaching.web.application.design.deanery.service.department.DepartmentService;
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

@Auth
@RestController
@AllArgsConstructor
@RequestMapping("departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    @Auth(isAdmin = true)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentDto create(@RequestBody BaseDepartmentDto department) {
        return departmentService.create(department);
    }

    @GetMapping
    @Operation(
            parameters = {
                    @Parameter(name = "name"),
                    @Parameter(name = "facultyId"),
                    @Parameter(name = "info")
            }
    )
    public List<DepartmentDto> findAll(@Parameter(hidden = true) DepartmentFilterOptions filterOptions,
                                       @Parameter(name = "limit") @RequestParam(required = false) Integer limit,
                                       @Parameter(name = "offset") @RequestParam(required = false) Integer offset) {
        return departmentService.findAll(filterOptions, limit, offset);
    }

    @GetMapping("count")
    @Operation(
            parameters = {
                    @Parameter(name = "name"),
                    @Parameter(name = "facultyId"),
                    @Parameter(name = "info")
            }
    )
    public ValueDto<Integer> count(@Parameter(hidden = true) DepartmentFilterOptions filterOptions) {
        return departmentService.count(filterOptions);
    }

    @GetMapping("{id}")
    public DepartmentDto find(@PathVariable Long id) {
        return departmentService.find(id);
    }

    @Auth(isAdmin = true)
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody BaseDepartmentDto departmentDto) {
        departmentService.update(id, departmentDto);
    }

    @Auth(isAdmin = true)
    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void patch(@PathVariable Long id, @RequestBody DepartmentPatch departmentPatch) {
        departmentService.patch(id, departmentPatch);
    }

    @Auth(isAdmin = true)
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        departmentService.delete(id);
    }
}
