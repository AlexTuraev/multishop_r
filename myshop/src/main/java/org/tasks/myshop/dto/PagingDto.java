package org.tasks.myshop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PagingDto {

    private int pageSize;
    private int pageNumber;
    private int totalPages;
    private boolean hasNextPage;
    private boolean hasPreviousPage;

    public PagingDto(int pageSize, int pageNumber, int totalPages) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.totalPages = totalPages;

        this.hasPreviousPage = pageNumber > 1;
        this.hasNextPage = pageNumber < totalPages;
    }

}
