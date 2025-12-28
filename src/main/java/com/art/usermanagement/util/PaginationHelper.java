package com.art.usermanagement.util;

import com.art.usermanagement.dto.request.ProjectSearchQueryParam;
import com.art.usermanagement.dto.response.PaginationDto;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

public class PaginationHelper {

    public static <T> PaginationDto buildPagination(int page, int size, long totalItems, List<?> content)
    {
        long totalPages = totalItems / (long) size < 1 ? 1 : (totalItems / (long) size);

        String nextPageUrl = (page + 1) <= totalPages ? constructUri(page + 1) : null;
        String prevPageUrl = (page - 1) >= 1 && (page - 1) <= totalPages ?
                constructUri(page - 1) : null;


        PaginationDto.PaginatedMetaData paginatedMetaData = PaginationDto.PaginatedMetaData.builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(Long.valueOf(totalPages).intValue())
                .totalItems(Long.valueOf(totalItems).intValue())
                .prevPageUrl(prevPageUrl)
                .nextPageUrl(nextPageUrl)
                .build();

        return PaginationDto.builder()
                .content(content)
                .pagination(paginatedMetaData)
                .build();
    }

    private static String constructUri(int page)
    {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replaceQueryParam("page", page)
                .toUriString();
    }

}
