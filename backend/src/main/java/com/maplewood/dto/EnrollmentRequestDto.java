package com.maplewood.dto;

import jakarta.validation.constraints.NotNull;

public record EnrollmentRequestDto(
        @NotNull Long studentId,
        @NotNull Long sectionId
) {}