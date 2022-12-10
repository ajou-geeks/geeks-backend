package com.geeks.geeksbackend.controller;

import com.geeks.geeksbackend.dto.delivery.*;
import com.geeks.geeksbackend.service.DeliveryService;
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
import org.springframework.web.bind.annotation.*;

@Tag(name = "delivery", description = "배달음식 공동구매 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final UserService userService;

    @Operation(summary = "POST() /delivery", description = "배달음식 공동구매 생성 API")
    @Parameters({
            @Parameter(name = "name", description = "배달음식 이름", example = "김밥천국 아주대점"),
            @Parameter(name = "type1", description = "배달음식 타입", example = "분식"),
            @Parameter(name = "minAmount", description = "최소주문금액", example = "15000"),
            @Parameter(name = "amount", description = "주문금액", example = "8000"),
            @Parameter(name = "startTime", description = "시작시각", example = "2022-11-26T11:00:00"),
            @Parameter(name = "endTime", description = "종료시각", example = "2022-11-26 12:30:00"),
            @Parameter(name = "destination", description = "소집장소", example = "남제관"),
            @Parameter(name = "thumbnailUrl", description = "썸네일 URL", example = "https://geeks-new-bucket.s3.ap-northeast-2.amazonaws.com/image/aaa.jpeg")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = DeliveryDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("")
    public ResponseEntity<DeliveryDto> createDelivery(@RequestBody DeliveryDto input) {
        Long userId = userService.getMyUserWithAuthorities().getId();
        DeliveryDto deliveryDto = deliveryService.createDelivery(input, userId);
        return new ResponseEntity<>(deliveryDto, HttpStatus.CREATED);
    }

    @Operation(summary = "PUT() /delivery/{id}", description = "배달음식 공동구매 수정 API")
    @Parameters({
            @Parameter(name = "name", description = "배달음식 이름", example = "김밥천국 아주대점"),
            @Parameter(name = "type1", description = "배달음식 타입", example = "분식"),
            @Parameter(name = "minAmount", description = "최소주문금액", example = "15000"),
            @Parameter(name = "amount", description = "주문금액", example = "6500"),
            @Parameter(name = "startTime", description = "시작시각", example = "2022-11-26T11:00:00"),
            @Parameter(name = "endTime", description = "종료시각", example = "2022-11-26 12:30:00"),
            @Parameter(name = "destination", description = "소집장소", example = "남제관"),
            @Parameter(name = "thumbnailUrl", description = "썸네일 URL", example = "https://geeks-new-bucket.s3.ap-northeast-2.amazonaws.com/image/aaa.jpeg")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = DeliveryDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DeliveryDto> updateDelivery(@PathVariable("id") Long id, @RequestBody DeliveryDto input) {
        Long userId = userService.getMyUserWithAuthorities().getId();
        DeliveryDto deliveryDto = deliveryService.updateDelivery(id, input, userId);
        return new ResponseEntity<>(deliveryDto, HttpStatus.OK);
    }

    @Operation(summary = "DELETE() /delivery/{id}", description = "배달음식 공동구매 삭제 API")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity deleteDelivery(@PathVariable("id") Long id) {
        deliveryService.deleteDelivery(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "GET() /delivery/{id}", description = "배달음식 공동구매 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = DeliveryDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDto> getDelivery(@PathVariable("id") Long id) {
        DeliveryDto deliveryDto = deliveryService.getDelivery(id);
        return new ResponseEntity<>(deliveryDto, HttpStatus.OK);
    }

    @Operation(summary = "GET() /delivery/list?page={}&size={}&sort={}&query={}", description = "배달음식 공동구매 목록 조회 API")
    @Parameters({
            @Parameter(name = "page", description = "검색할 페이지 (기본 0, 최대 1000)", example = "0"),
            @Parameter(name = "size", description = "한번에 검색할 원소 갯수 (기본 10, 최대 100)", example = "10"),
            @Parameter(name = "sort", description = "정렬 방법", example = "id"),
            @Parameter(name = "query", description = "검색할 내용", example = "아주대")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = DeliveryListDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/list")
    public ResponseEntity<DeliveryListDto> getDeliveryList(
            @RequestParam(required = false) String query, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        DeliveryListDto deliveryListDto = deliveryService.getDeliveryList(query, pageable);
        return new ResponseEntity<>(deliveryListDto, HttpStatus.OK);
    }

    @Operation(summary = "POST() /delivery/join", description = "배달음식 공동구매 참여 API")
    @Parameters({
            @Parameter(name = "id", description = "배달음식ID", example = "1"),
            @Parameter(name = "amount", description = "주문금액", example = "7000"),
            @Parameter(name = "description", description = "주문상세", example = "떡볶이 1개, 참치마요김밥 1개")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = DeliveryDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/join")
    public ResponseEntity<DeliveryDto> joinDelivery(@RequestBody JoinDeliveryDto input) {
        Long userId = userService.getMyUserWithAuthorities().getId();
        DeliveryDto deliveryDto = deliveryService.joinDelivery(input, userId);
        return new ResponseEntity<>(deliveryDto, HttpStatus.OK);
    }

    @Operation(summary = "POST() /delivery/cancel", description = "배달음식 공동구매 참여 취소 API")
    @Parameters({
            @Parameter(name = "id", description = "배달음식ID", example = "1")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = DeliveryDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/cancel")
    public ResponseEntity<DeliveryDto> cancelDelivery(@RequestBody DeliveryIdDto input) {
        Long userId = userService.getMyUserWithAuthorities().getId();
        DeliveryDto deliveryDto = deliveryService.cancelDelivery(input.getId(), userId);
        return new ResponseEntity<>(deliveryDto, HttpStatus.OK);
    }

    @Operation(summary = "POST() /delivery/settle", description = "배달음식 공동구매 정산 API")
    @Parameters({
            @Parameter(name = "id", description = "배달음식ID", example = "1"),
            @Parameter(name = "bankName", description = "은행이름", example = "카카오뱅크"),
            @Parameter(name = "accountNumber", description = "계좌번호", example = "1111-11-1111111"),
            @Parameter(name = "totalAmount", description = "총 결제금액", example = "20000")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = DeliveryDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/settle")
    public ResponseEntity<DeliveryDto> settleDelivery(@RequestBody SettleDeliveryDto input) {
        Long userId = userService.getMyUserWithAuthorities().getId();
        DeliveryDto deliveryDto = deliveryService.settleDelivery(input, userId);
        return new ResponseEntity<>(deliveryDto, HttpStatus.OK);
    }

    @Operation(summary = "POST() /delivery/receive", description = "배달음식 공동구매 수령 API")
    @Parameters({
            @Parameter(name = "id", description = "배달음식ID", example = "1"),
            @Parameter(name = "pickupLocation", description = "수령장소", example = "일신관 로비"),
            @Parameter(name = "pickupDatetime", description = "수령일자", example = "2022-11-27T18:00:00")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = DeliveryDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/receive")
    public ResponseEntity<DeliveryDto> receiveDelivery(@RequestBody ReceiveDeliveryDto input) {
        Long userId = userService.getMyUserWithAuthorities().getId();
        DeliveryDto deliveryDto = deliveryService.receiveDelivery(input, userId);
        return new ResponseEntity<>(deliveryDto, HttpStatus.OK);
    }

    @Operation(summary = "POST() /delivery/confirm", description = "배달음식 공동구매 완료 API")
    @Parameters({
            @Parameter(name = "id", description = "물품ID", example = "1")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = DeliveryDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/confirm")
    public ResponseEntity<DeliveryDto> confirmDelivery(@RequestBody DeliveryIdDto input) {
        Long userId = userService.getMyUserWithAuthorities().getId();
        DeliveryDto deliveryDto = deliveryService.confirmDelivery(input.getId(), userId);
        return new ResponseEntity<>(deliveryDto, HttpStatus.OK);
    }
}
