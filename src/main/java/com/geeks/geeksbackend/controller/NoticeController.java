package com.geeks.geeksbackend.controller;

import com.geeks.geeksbackend.dto.notice.NoticeListDto;
import com.geeks.geeksbackend.service.NoticeService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "notice", description = "알림 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final UserService userService;

    @Operation(summary = "GET() /notice?page={}&size={}", description = "알림 조회 API")
    @Parameters({
            @Parameter(name = "page", description = "검색할 페이지 (기본 0, 최대 1000)", example = "0"),
            @Parameter(name = "size", description = "한번에 검색할 원소 갯수 (기본 10, 최대 100)", example = "10"),
            @Parameter(name = "sort", description = "정렬 방법", example = "id")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = NoticeListDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("")
    public ResponseEntity<NoticeListDto> getNoticeList(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = userService.getMyUserWithAuthorities().getId();
        NoticeListDto noticeListDto = noticeService.getNoticeList(userId, pageable);
        return new ResponseEntity<>(noticeListDto, HttpStatus.OK);
    }
}
