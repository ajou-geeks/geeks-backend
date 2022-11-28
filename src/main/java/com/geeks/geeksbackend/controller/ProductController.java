package com.geeks.geeksbackend.controller;

import com.geeks.geeksbackend.dto.ProductDto;
import com.geeks.geeksbackend.dto.UserDto;
import com.geeks.geeksbackend.service.MemberService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "product", description = "물품 공동구매 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final MemberService memberService;

    @Operation(summary = "POST() /products", description = "물품 공동구매 생성 API")
    @Parameters({
            @Parameter(name = "name", description = "물품이름", example = "[폴바셋] 바리스타 돌체라떼 330mL"),
            @Parameter(name = "type1", description = "물품타입", example = "음료"),
            @Parameter(name = "price", description = "물품가격", example = "3400"),
            @Parameter(name = "startTime", description = "시작시각", example = "2022-11-26T11:44:30"),
            @Parameter(name = "endTime", description = "종료시각", example = "2022-12-26T00:00:00"),
            @Parameter(name = "maxParticipant", description = "최대 참여인원", example = "5"),
            @Parameter(name = "destination", description = "소집장소", example = "남제관"),
            @Parameter(name = "thumbnailUrl", description = "썸네일 URL", example = "https://geeks-new-bucket.s3.ap-northeast-2.amazonaws.com/image/dolce-latte.jpeg")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto input) {
        UserDto userDto = memberService.getMyUserWithAuthorities();
        ProductDto productDto = productService.createProduct(input, userDto);
        return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }

    @Operation(summary = "GET()", description = "index")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 성공")
    })
    @GetMapping("")
    public String hello() {
        return "hello";
    }
}
