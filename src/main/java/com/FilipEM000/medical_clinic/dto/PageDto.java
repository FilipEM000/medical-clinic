package com.FilipEM000.medical_clinic.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public class PageDto<T> {
    private List<T> content;
    private Integer totalPages;
    private Long totalElements;

    public PageDto(Page<T> page) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
    }
}
