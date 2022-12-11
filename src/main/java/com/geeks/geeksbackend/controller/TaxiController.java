package com.geeks.geeksbackend.controller;

import com.geeks.geeksbackend.dto.taxi.ChangeDto;
import com.geeks.geeksbackend.dto.taxi.CreateDto;
import com.geeks.geeksbackend.entity.Taxi;
import com.geeks.geeksbackend.service.MemberService;
import com.geeks.geeksbackend.service.TaxiService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "taxi", description = "공동구매 택시 API")
@RestController
@RequestMapping("/taxi")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class TaxiController {

    private final MemberService memberService;
    private final TaxiService taxiService;

    @Operation(summary = "GET() /taxi", description = "택시 공동구매 리스트 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 성공")
    })
    @GetMapping("")
    public ResponseEntity getTaxis() {
        return ResponseEntity.ok().body(taxiService.getTaxis());
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
        return ResponseEntity.ok().body(taxiService.getTaxi(id));
    }

    @Operation(summary = "GET() /taxi/member/{id}", description = "택시 공동구매 참여자 조회 API")
    @Parameters({
            @Parameter(name = "id", description = "택시 아이디", example = "1")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "택시 공동구매 참여자 조회 성공")
    })
    @GetMapping("/member/{id}")
    public ResponseEntity<?> getTaxiMembers(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(taxiService.getTaxiMembers(id));
    }

    @Operation(summary = "POST() /taxi", description = "택시 공동구매 생성 API")
    @Parameters({
            @Parameter(name = "userId", description = "공동구매 생성한 유저 아이디", example = "1"),
            @Parameter(name = "price", description = "예상 가격", example = "4950"),
            @Parameter(name = "startTime", description = "시작시각", example = "2022-11-26T11:44:30"),
            @Parameter(name = "endTime", description = "종료시각", example = "2022-12-26T00:00:00"),
            @Parameter(name = "maxParticipant", description = "최대 참여인원", example = "4"),
            @Parameter(name = "source", description = "출발지", example = "아주대 일신관 앞"),
            @Parameter(name = "destination", description = "목적지", example = "수원역"),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "택시 공동구매 생성 완료", content = @Content(schema = @Schema(implementation = CreateDto.class))),
            @ApiResponse(responseCode = "400", description = "택시 공동구매 생성 실패")
    })
    @PostMapping("")
    public ResponseEntity<?> createTaxi(@RequestBody CreateDto createDto) {
        try {
            taxiService.createTaxi(createDto);
            return ResponseEntity.ok().body(createDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("택시 공동구매 생성 실패");
        }
    }

    @Operation(summary = "PATCH() /taxi/cancle", description = "택시 공동구매 취소 API")
    @Parameters({
            @Parameter(name = "id", description = "택시 공동구매 아이디", example = "1"),
            @Parameter(name = "userId", description = "공동구매를 취소하고자 하는 유저의 아이디", example = "1")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "택시 공동구매 취소 완료", content = @Content(schema = @Schema(implementation = Taxi.class))),
            @ApiResponse(responseCode = "400", description = "택시 공동구매 취소 실패")
    })
    @PatchMapping("/cancle")
    public ResponseEntity<?> cancleTaxi(@RequestBody ChangeDto changeDto) {
        try {
            boolean success = taxiService.cancleTaxi(changeDto);
            if (success) {
                return ResponseEntity.ok().body("택시 공동구매 취소 완료");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("공동구매 생성자만 공동구매를 취소할 수 있습니다.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("택시 공동구매 취소 실패");
        }
    }

    @Operation(summary = "PATCH() /taxi/complete", description = "택시 공동구매 마감 API")
    @Parameters({
            @Parameter(name = "id", description = "택시 공동구매 아이디", example = "1"),
            @Parameter(name = "userId", description = "공동구매를 취소하고자 하는 유저의 아이디", example = "1")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "택시 공동구매 마감 완료", content = @Content(schema = @Schema(implementation = Taxi.class))),
            @ApiResponse(responseCode = "400", description = "택시 공동구매 마감 실패")
    })
    @PatchMapping("/complete")
    public ResponseEntity<?> completeTaxi(@RequestBody ChangeDto changeDto) {
        try {
            boolean success = taxiService.completeTaxi(changeDto);
            if (success) {
                return ResponseEntity.ok().body("택시 공동구매 마감 완료");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("공동구매 생성자만 공동구매를 마감할 수 있습니다.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("택시 공동구매 마감 실패");
        }
    }

    @Operation(summary = "POST() /taxi/join", description = "택시 공동구매 참여 API")
    @Parameters({
            @Parameter(name = "id", description = "택시 아이디", example = "1"),
            @Parameter(name = "userId", description = "유저 아이디", example = "1")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "택시 공동구매 참여 완료", content = @Content(schema = @Schema(implementation = ChangeDto.class))),
            @ApiResponse(responseCode = "400", description = "택시 공동구매 참여 실패")
    })
    @PostMapping("/join")
    public ResponseEntity<?> joinTaxi(@RequestBody ChangeDto changeDto) {
        try {
            int result = taxiService.joinTaxi(changeDto);
            switch (result) {
                case 1:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("참여기간 초과");
                case 2:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("참여 가능한 상태가 아닌 공동구매");
                case 3:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 참여한 공동구매");
                case 4:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 공동구매");
                case 5:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 유저");
                default:
                    return ResponseEntity.ok().body("참여 성공");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("택시 공동구매 참여 실패");
        }
    }
}
