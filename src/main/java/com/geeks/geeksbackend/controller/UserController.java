package com.geeks.geeksbackend.controller;

import com.geeks.geeksbackend.dto.user.UserInfoDto;
import com.geeks.geeksbackend.dto.user.UserProfileDto;
import com.geeks.geeksbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "user", description = "유저 관련 API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "GET() /user/{id}", description = "유저 정보 조회")
    @Parameters({
            @Parameter(name = "id", description = "회원 아이디", example = "1")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 정보 조회 성공", content = @Content(schema = @Schema(implementation = UserInfoDto.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable(value = "id", required = false) long id) {
        return ResponseEntity.ok().body(userService.findById(id));
    }

    @Operation(summary = "PATCH() /user/{id}", description = "유저 정보 수정")
    @Parameters({
            @Parameter(name = "id", description = "회원 아이디", example = "1"),
            @Parameter(name = "bio", description = "자기소개", example = "안녕하세요"),
            @Parameter(name = "pattern", description = "생활패턴", example = "야행성"),
            @Parameter(name = "patternDetail", description = "생활패턴 설명", example = "새벽 4시에 자는 편입니다.")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "유저 정보 수정 실패")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") long id,
                                          @RequestBody UserProfileDto userProfileDto) {
        try {
            userService.update(id, userProfileDto);
            return ResponseEntity.ok().body("유저 정보 수정 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유저 정보 수정 실패");
        }
    }

    @Operation(summary = "PATCH() /user/profile/{id}", description = "유저 프로필 이미지 수정")
    @Parameters({
            @Parameter(name = "id", description = "회원 아이디", example = "1"),
            @Parameter(name = "profile", description = "프로필 사진", example = "abc.jpg")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 프로필 이미지 수정 성공"),
            @ApiResponse(responseCode = "400", description = "유저 프로필 이미지 수정 실패")
    })
    @PatchMapping("/profile/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable("id") long id,
                                           @RequestPart(value = "profile") MultipartFile multipartFile) {
        try {
            userService.update(id, multipartFile);
            return ResponseEntity.ok().body("유저 프로필 이미지 수정 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유저 프로필 이미지 수정 실패");
        }
    }
}
