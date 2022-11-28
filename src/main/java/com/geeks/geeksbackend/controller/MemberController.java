package com.geeks.geeksbackend.controller;

import com.geeks.geeksbackend.entity.Member;
import com.geeks.geeksbackend.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Tag(name = "member", description = "유저 관련 API")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "GET() /member/{id}", description = "마이페이지 조회")
    @Parameters({
            @Parameter(name = "id", description = "회원 아이디", example = "1")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "마이페이지 조회 성공", content = @Content(schema = @Schema(implementation = Member.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getMember(@PathVariable("id") long id) {
        return ResponseEntity.ok().body("");
    }
}
