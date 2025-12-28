package com.art.usermanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginationDto {
    private List<?> content;
    private PaginatedMetaData pagination;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PaginatedMetaData {
        private int currentPage;
        private int pageSize;
        private int totalPages;
        private long totalItems;
        private String nextPageUrl;
        private String prevPageUrl;
    }
}
