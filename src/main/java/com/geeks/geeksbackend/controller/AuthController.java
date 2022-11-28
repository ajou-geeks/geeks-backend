package com.geeks.geeksbackend.controller;

import com.geeks.geeksbackend.dto.LoginDto;
import com.geeks.geeksbackend.dto.TokenDto;
import com.geeks.geeksbackend.dto.UserDto;
import com.geeks.geeksbackend.jwt.JwtFilter;
import com.geeks.geeksbackend.jwt.TokenProvider;
import com.geeks.geeksbackend.service.FileService;
import com.geeks.geeksbackend.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberService memberService;
    private final FileService fileService;

    @Operation(summary = "POST() /auth/register", description = "회원가입 API")
    @Parameters({
            @Parameter(name = "email", description = "이메일", example = "abc123"),
            @Parameter(name = "password", description = "비밀번호", example = "a1b2c3"),
            @Parameter(name = "file", description = "입사확인서", example = "입사확인서.pdf")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = UserDto.class)))
    })
    @PostMapping(value = "/register", consumes = "multipart/form-data")
    public ResponseEntity<?> signup(
            @Valid @ModelAttribute UserDto userDto
    ) throws IOException {
        String savedName = fileService.saveFile(userDto.getFile());
        memberService.signup(userDto, savedName);
        return ResponseEntity.ok().body(userDto.getEmail() + " " + savedName);
    }

    @Operation(summary = "POST() /auth/login", description = "로그인 API")
    @Parameters({
            @Parameter(name = "name", description = "아이디", example = "abc123"),
            @Parameter(name = "password", description = "비밀번호", example = "a1b2c3")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = TokenDto.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }
}