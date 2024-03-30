package org.englising.com.englisingbe.global.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDto {
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private int numberOfElements;
    private boolean isFirst;
    private boolean isLast;

    public static PaginationDto from(Page<?> page){
        return PaginationDto.builder()
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .numberOfElements(page.getNumberOfElements())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .build();
    }
}