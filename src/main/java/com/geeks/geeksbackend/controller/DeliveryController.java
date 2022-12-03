package com.geeks.geeksbackend.controller;

import com.geeks.geeksbackend.dto.delivery.DeliveryDto;
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
}
