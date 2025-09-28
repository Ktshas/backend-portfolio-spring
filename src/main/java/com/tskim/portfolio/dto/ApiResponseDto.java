package com.tskim.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto<T> {
    
    private boolean success;
    private T data;
    private String message;
    
    /**
     * 성공 응답 생성
     */
    public static <T> ApiResponseDto<T> success(T data) {
        return ApiResponseDto.<T>builder()
                .success(true)
                .data(data)
                .message("요청이 성공적으로 처리되었습니다")
                .build();
    }
    
    /**
     * 성공 응답 생성 (커스텀 메시지)
     */
    public static <T> ApiResponseDto<T> success(T data, String message) {
        return ApiResponseDto.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .build();
    }
    
    /**
     * 실패 응답 생성
     */
    public static <T> ApiResponseDto<T> error(String message) {
        return ApiResponseDto.<T>builder()
                .success(false)
                .data(null)
                .message(message)
                .build();
    }
    
    /**
     * 실패 응답 생성 (에러 데이터 포함)
     */
    public static <T> ApiResponseDto<T> error(T data, String message) {
        return ApiResponseDto.<T>builder()
                .success(false)
                .data(data)
                .message(message)
                .build();
    }
}
