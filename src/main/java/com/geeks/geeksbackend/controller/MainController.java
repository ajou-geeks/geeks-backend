package com.geeks.geeksbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "main", description = "비회원 유저 처리 API")
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {

    @Operation(summary = "GET()", description = "index")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 성공")
    })
    @GetMapping("")
    public String hello() {
        return "hello";
    }
}
