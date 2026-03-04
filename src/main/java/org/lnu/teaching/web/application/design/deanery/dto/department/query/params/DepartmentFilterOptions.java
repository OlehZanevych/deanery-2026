package org.lnu.teaching.web.application.design.deanery.dto.department.query.params;

import lombok.Data;

@Data
public class DepartmentFilterOptions {
    private String name;
    private Long facultyId;
    private String info;
}
