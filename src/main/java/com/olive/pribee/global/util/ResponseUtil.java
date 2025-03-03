package com.olive.pribee.global.util;

import java.io.IOException;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olive.pribee.global.common.DataResponseDto;
import com.olive.pribee.global.common.ResponseDto;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResponseUtil {

    // 단순 메시지 응답을 설정
    public static void setResponse(HttpServletResponse response, int status, String message) throws IOException {
        ResponseDto dto = ResponseDto.of(status, message);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(status);
        response.getWriter().write(new ObjectMapper().writeValueAsString(dto));
    }

    // 데이터가 포함된 응답을 설정
    public static void setDataResponse(HttpServletResponse response, int status, Object data) throws IOException {
        ResponseDto dto = DataResponseDto.of(data, status);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(status);
        response.getWriter().write(new ObjectMapper().writeValueAsString(dto));
    }
}
