package com.geeks.geeksbackend.controller;

import com.geeks.geeksbackend.dto.user.UserInfoDto;
import com.geeks.geeksbackend.dto.user.UserInfoListDto;
import com.geeks.geeksbackend.dto.user.UserProfileDto;
import com.geeks.geeksbackend.service.RoommateService;
import com.geeks.geeksbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "roommate", description = "룸메이트 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/roommate")
@RequiredArgsConstructor
public class RoommateController {

    private final RoommateService roommateService;
    private final UserService userService;

    @Operation(summary = "POST() /roommate/profile", description = "룸메이트 프로필 생성 API")
    @Parameters({
            @Parameter(name = "bio", description = "자기소개", example = "안녕하세요 저는 맥도날드를 좋아합니다."),
            @Parameter(name = "characterType", description = "성격타입", example = "[\"외향적인\", \"재미있는\"]"),
            @Parameter(name = "pattern", description = "생활패턴", example = "늑대형"),
            @Parameter(name = "patternDetail", description = "생활패턴 상세", example = "저는 일찍 일어나고 늦게 잡니다."),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = UserInfoDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/profile")
    public ResponseEntity<UserInfoDto> createProfile(@RequestBody UserProfileDto input) {
        Long userId = userService.getMyUserWithAuthorities().getId();
        UserInfoDto userInfoDto = roommateService.createProfile(input, userId);
        return new ResponseEntity<>(userInfoDto, HttpStatus.CREATED);
    }

    @Operation(summary = "POST() /roommate/profile", description = "룸메이트 프로필 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "CREATED", content = @Content(schema = @Schema(implementation = UserInfoDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/profile/{id}")
    public ResponseEntity<UserInfoDto> getProfile(@PathVariable("id") Long id) {
        UserInfoDto userInfoDto = roommateService.getProfile(id);
        return new ResponseEntity<>(userInfoDto, HttpStatus.OK);
    }

    @Operation(summary = "GET() /roommate/list", description = "룸메이트 목록 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserInfoListDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/list")
    public ResponseEntity<UserInfoListDto> getProfileList() {
        Long userId = userService.getMyUserWithAuthorities().getId();
        UserInfoListDto userInfoListDto = roommateService.getProfileList(userId);
        return new ResponseEntity<>(userInfoListDto, HttpStatus.OK);
    }
}
