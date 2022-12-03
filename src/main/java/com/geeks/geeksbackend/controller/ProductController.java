package com.geeks.geeksbackend.controller;

import com.geeks.geeksbackend.dto.product.ProductDto;
import com.geeks.geeksbackend.dto.product.ProductListDto;
import com.geeks.geeksbackend.dto.user.UserDto;
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

@Tag(name = "product", description = "물품 공동구매 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    @Operation(summary = "POST() /product", description = "물품 공동구매 생성 API")
    @Parameters({
            @Parameter(name = "name", description = "물품이름", example = "춘식이 바나나우유 500ML x 3개"),
            @Parameter(name = "type1", description = "물품타입", example = "음료"),
            @Parameter(name = "price", description = "물품가격", example = "4950"),
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
        UserDto userDto = userService.getMyUserWithAuthorities();
        ProductDto productDto = productService.createProduct(input, userDto.getId());
        return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }

    @Operation(summary = "PUT() /product/{id}", description = "물품 공동구매 수정 API")
    @Parameters({
            @Parameter(name = "name", description = "물품이름", example = "춘식이 초코우유 500ML x 3개"),
            @Parameter(name = "type1", description = "물품타입", example = "음료"),
            @Parameter(name = "price", description = "물품가격", example = "4500"),
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
        UserDto userDto = userService.getMyUserWithAuthorities();
        ProductDto productDto = productService.updateProduct(id, input, userDto.getId());
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @Operation(summary = "DELETE() /product/{id}", description = "물품 공동구매 삭제 API")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable("id") Long id) {
        UserDto userDto = userService.getMyUserWithAuthorities();
        productService.deleteProduct(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "GET() /product/{id}", description = "물품 공동구매 조회 API")
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

    @Operation(summary = "GET() /product/list", description = "물품 공동구매 목록 조회 API")
    @Parameters({
            @Parameter(name = "page", description = "검색할 페이지 (기본 1, 최대 1000)", example = "1"),
            @Parameter(name = "count", description = "한번에 검색할 원소 갯수 (기본 10, 최대 100)", example = "10"),
            @Parameter(name = "sort", description = "정렬 방법", example = "recent"),
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
            @RequestParam String query, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        ProductListDto productListDto = productService.getProductList(query, pageable);
        return new ResponseEntity<>(productListDto, HttpStatus.OK);
    }
}
