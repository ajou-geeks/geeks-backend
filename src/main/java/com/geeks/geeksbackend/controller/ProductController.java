package com.geeks.geeksbackend.controller;

import com.geeks.geeksbackend.dto.product.*;
import com.geeks.geeksbackend.service.UserService;
import com.geeks.geeksbackend.service.ProductService;
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

@Tag(name = "product", description = "생필품 공동구매 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    @Operation(summary = "POST() /product", description = "생필품 공동구매 생성 API")
    @Parameters({
            @Parameter(name = "name", description = "생필품 이름", example = "춘식이 바나나우유 500ML x 3개"),
            @Parameter(name = "type1", description = "생필품 타입", example = "음료"),
            @Parameter(name = "price", description = "생필품 가격", example = "4950"),
            @Parameter(name = "startTime", description = "시작시각", example = "2022-11-26T11:44:30"),
            @Parameter(name = "endTime", description = "종료시각", example = "2022-12-26T00:00:00"),
            @Parameter(name = "maxParticipant", description = "최대 참여인원", example = "3"),
            @Parameter(name = "destination", description = "소집장소", example = "남제관"),
            @Parameter(name = "thumbnailUrl", description = "썸네일 URL", example = "https://geeks-new-bucket.s3.ap-northeast-2.amazonaws.com/image/aaa.jpeg")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto input) {
        Long userId = userService.getMyUserWithAuthorities().getId();
        ProductDto productDto = productService.createProduct(input, userId);
        return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }

    @Operation(summary = "PUT() /product/{id}", description = "생필품 공동구매 수정 API")
    @Parameters({
            @Parameter(name = "name", description = "생필품 이름", example = "춘식이 초코우유 500ML x 3개"),
            @Parameter(name = "type1", description = "생필품 타입", example = "음료"),
            @Parameter(name = "price", description = "생필품 가격", example = "4500"),
            @Parameter(name = "startTime", description = "시작시각", example = "2022-11-26T12:00:00"),
            @Parameter(name = "endTime", description = "종료시각", example = "2022-12-26T00:00:00"),
            @Parameter(name = "maxParticipant", description = "최대 참여인원", example = "3"),
            @Parameter(name = "destination", description = "소집장소", example = "남제관"),
            @Parameter(name = "thumbnailUrl", description = "썸네일 URL", example = "https://geeks-new-bucket.s3.ap-northeast-2.amazonaws.com/image/bbb.jpeg")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("id") Long id, @RequestBody ProductDto input) {
        Long userId = userService.getMyUserWithAuthorities().getId();
        ProductDto productDto = productService.updateProduct(id, input, userId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @Operation(summary = "DELETE() /product/{id}", description = "생필품 공동구매 삭제 API")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable("id") Long id) {
        Long userId = userService.getMyUserWithAuthorities().getId();
        productService.deleteProduct(id, userId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "GET() /product/{id}", description = "생필품 공동구매 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable("id") Long id) {
        ProductDto productDto = productService.getProduct(id);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @Operation(summary = "GET() /product/list?page={}&size={}&sort={}&query={}", description = "생필품 공동구매 목록 조회 API")
    @Parameters({
            @Parameter(name = "page", description = "검색할 페이지 (기본 0, 최대 1000)", example = "0"),
            @Parameter(name = "size", description = "한번에 검색할 원소 갯수 (기본 10, 최대 100)", example = "10"),
            @Parameter(name = "sort", description = "정렬 방법", example = "id"),
            @Parameter(name = "query", description = "검색할 내용", example = "우유")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ProductListDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/list")
    public ResponseEntity<ProductListDto> getProductList(
            @RequestParam(required = false) String query, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        ProductListDto productListDto = productService.getProductList(query, pageable);
        return new ResponseEntity<>(productListDto, HttpStatus.OK);
    }

    @Operation(summary = "POST() /product/join", description = "생필품 공동구매 참여 API")
    @Parameters({
            @Parameter(name = "id", description = "생필품 ID", example = "1")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/join")
    public ResponseEntity<ProductDto> joinProduct(@RequestBody ProductIdDto input) {
        Long userId = userService.getMyUserWithAuthorities().getId();
        ProductDto productDto = productService.joinProduct(input.getId(), userId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @Operation(summary = "POST() /product/cancel", description = "생필품 공동구매 참여 취소 API")
    @Parameters({
            @Parameter(name = "id", description = "생필품 ID", example = "1")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/cancel")
    public ResponseEntity<ProductDto> cancelProduct(@RequestBody ProductIdDto input) {
        Long userId = userService.getMyUserWithAuthorities().getId();
        ProductDto productDto = productService.cancelProduct(input.getId(), userId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @Operation(summary = "POST() /product/close", description = "생필품 공동구매 마감 API")
    @Parameters({
            @Parameter(name = "id", description = "생필품 ID", example = "1")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/close")
    public ResponseEntity<ProductDto> closeProduct(@RequestBody ProductIdDto input) {
        Long userId = userService.getMyUserWithAuthorities().getId();
        ProductDto productDto = productService.closeProduct(input.getId(), userId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @Operation(summary = "POST() /product/settle", description = "생필품 공동구매 정산 API")
    @Parameters({
            @Parameter(name = "id", description = "생필품 ID", example = "1"),
            @Parameter(name = "bankName", description = "은행이름", example = "카카오뱅크"),
            @Parameter(name = "accountNumber", description = "계좌번호", example = "1111-11-1111111"),
            @Parameter(name = "totalAmount", description = "총 결제금액", example = "20000"),
            @Parameter(name = "amount", description = "정산금액", example = "5000")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/settle")
    public ResponseEntity<ProductDto> settleProduct(@RequestBody SettleProductDto input) {
        Long userId = userService.getMyUserWithAuthorities().getId();
        ProductDto productDto = productService.settleProduct(input, userId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @Operation(summary = "POST() /product/receive", description = "생필품 공동구매 수령 API")
    @Parameters({
            @Parameter(name = "id", description = "생필품 ID", example = "1"),
            @Parameter(name = "pickupLocation", description = "수령장소", example = "일신관 로비"),
            @Parameter(name = "pickupDatetime", description = "수령일자", example = "2022-11-27T18:00:00")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/receive")
    public ResponseEntity<ProductDto> receiveProduct(@RequestBody ReceiveProductDto input) {
        Long userId = userService.getMyUserWithAuthorities().getId();
        ProductDto productDto = productService.receiveProduct(input, userId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @Operation(summary = "POST() /product/confirm", description = "생필품 공동구매 완료 API")
    @Parameters({
            @Parameter(name = "id", description = "생필품 ID", example = "1")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/confirm")
    public ResponseEntity<ProductDto> confirmProduct(@RequestBody ProductIdDto input) {
        Long userId = userService.getMyUserWithAuthorities().getId();
        ProductDto productDto = productService.confirmProduct(input.getId(), userId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }
}
