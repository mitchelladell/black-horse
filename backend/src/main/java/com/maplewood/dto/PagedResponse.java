package com.maplewood.dto;

import org.springframework.data.domain.Page;
import java.util.List;

public record PagedResponse<T>(
    List<T> content,
    int page,
    int totalPages,
    long totalElements,
    boolean last
) {
    public static <T, S> PagedResponse<S> from(Page<T> source, List<S> mappedContent) {
        return new PagedResponse<>(
            mappedContent,
            source.getNumber(),
            source.getTotalPages(),
            source.getTotalElements(),
            source.isLast()
        );
}
}