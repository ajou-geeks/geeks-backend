package com.geeks.geeksbackend.controller;

import com.geeks.geeksbackend.service.MemberService;
import com.geeks.geeksbackend.service.TaxiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "taxi", description = "공동구매 택시 API")
@RestController
@RequestMapping("/taxi")
@RequiredArgsConstructor
public class TaxiController {

    private final MemberService memberService;
    private final TaxiService taxiService;

    @Operation(summary = "GET() /taxi", description = "택시 공동구매 리스트 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 성공")
    })
    @GetMapping("")
    public ResponseEntity getTaxis() {
        return ResponseEntity.ok().body(taxiService.findAll());
    }

    @Operation(summary = "GET() /taxi/{id}", description = "택시 공동구매 조회 API")
    @Parameters({
            @Parameter(name = "id", description = "택시 아이디", example = "1")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 성공")
    })
    @GetMapping("/{id}")
    public ResponseEntity getTaxi(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(taxiService.findById(id));
    }
}
