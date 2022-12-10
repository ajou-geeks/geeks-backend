package com.geeks.geeksbackend.controller;

import com.geeks.geeksbackend.dto.note.NoteListDto;
import com.geeks.geeksbackend.dto.note.SendNoteDto;
import com.geeks.geeksbackend.service.NoteService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "note", description = "쪽지 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/note")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    @Operation(summary = "POST() /note", description = "쪽지 보내기 API")
    @Parameters({
            @Parameter(name = "receiverId", description = "수신자 ID", example = "1"),
            @Parameter(name = "content", description = "쪽지 내용", example = "텍스트 영역입니다. 최대 200자 표시 가능합니다.")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = NoteListDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("")
    public ResponseEntity<NoteListDto> sendNote(@RequestBody SendNoteDto input) {
        Long userId = userService.getMyUserWithAuthorities().getId();
        NoteListDto noteListDto = noteService.sendNote(input, userId);
        return new ResponseEntity<>(noteListDto, HttpStatus.OK);
    }
}
