package com.geeks.geeksbackend.controller;

import com.geeks.geeksbackend.dto.user.LoginDto;
import com.geeks.geeksbackend.dto.jwt.TokenDto;
import com.geeks.geeksbackend.dto.user.UserDto;
import com.geeks.geeksbackend.jwt.JwtFilter;
import com.geeks.geeksbackend.jwt.TokenProvider;
import com.geeks.geeksbackend.service.AwsS3Service;
import com.geeks.geeksbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
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

@Tag(name = "auth", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;
    private final AwsS3Service awsS3Service;

    @Operation(summary = "POST() /auth/register", description = "회원가입 API")
    @Parameters({
            @Parameter(name = "email", description = "이메일", example = "abc123"),
            @Parameter(name = "password", description = "비밀번호", example = "a1b2c3"),
            @Parameter(name = "file", description = "입주확인서", example = "입주확인서.pdf")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공")
    })
    @PostMapping(value = "/register", consumes = "multipart/form-data")
    public ResponseEntity<?> signup(
            @Valid @ModelAttribute UserDto userDto
    ) throws IOException {
        String url = awsS3Service.uploadFileV1(userDto.getFile());
        userService.signup(userDto, url);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", userDto.getEmail());
        jsonObject.put("file", userDto.getFile().getOriginalFilename());
        return ResponseEntity.ok().body(jsonObject);
    }

    @Operation(summary = "POST() /auth/login", description = "로그인 API")
    @Parameters({
            @Parameter(name = "name", description = "아이디", example = "abc123"),
            @Parameter(name = "password", description = "비밀번호", example = "a1b2c3")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공")
    })
    @PostMapping("/login")
    public ResponseEntity<JSONObject> authorize(@Valid @RequestBody LoginDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        long id = userService.findByEmail(loginDto.getEmail()).getId();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("jwt", new TokenDto(jwt));

        return new ResponseEntity<>(jsonObject, httpHeaders, HttpStatus.OK);
    }
}
