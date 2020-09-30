package com.majorrunner.majorserver.Like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class LikeDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadLikeResponse {
        private String username;
        private LocalDateTime createdAt;
    }
}
