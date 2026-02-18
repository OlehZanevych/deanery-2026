package org.lnu.teaching.web.application.design.deanery.dto.common;

import lombok.Data;

@Data
public class ValueDto<T> {
    private final T value;
}
