package com.majorrunner.majorserver.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CategoryDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadCategoryResponse {
        private String categoryName;
        private String subCategoryName;
    }

}
