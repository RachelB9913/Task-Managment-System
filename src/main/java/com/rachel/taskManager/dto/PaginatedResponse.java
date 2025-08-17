package com.rachel.taskManager.dto;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PaginatedResponse<T> {
    // A generic DTO to represent a short paginated response for any type of content.

    private final List<T> content;
    private final int page;          // one-based
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final boolean last;

    public PaginatedResponse(List<T> content, int page, int size,
                             long totalElements, int totalPages, boolean last) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    public static <T> PaginatedResponse<T> from(Page<T> page) {
        return new PaginatedResponse<>(
                page.getContent(),
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    public static <E, D> PaginatedResponse<D> from(Page<E> page, Function<E, D> mapper) {
        List<D> mapped = page.getContent().stream().map(mapper).collect(Collectors.toList());
        return new PaginatedResponse<>(
                mapped,
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    public List<T> getContent() { return content; }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public long getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }
    public boolean isLast() { return last; }
}
