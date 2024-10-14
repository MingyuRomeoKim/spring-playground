package com.mingyu.playground.controllers;

import com.mingyu.playground.dto.request.SaveMemberRequestDto;
import com.mingyu.playground.dto.request.UpdateMemberRequestDto;
import com.mingyu.playground.dto.response.FindMemberResponseDto;
import com.mingyu.playground.services.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Member", description = "Member 관련 Controller 입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 등록", description = "회원 정보가 등록됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Description("Save Member")
    @PostMapping("/member")
    public ResponseEntity<?> saveMember(@RequestBody SaveMemberRequestDto saveMemberRequestDto) {
        memberService.saveMember(saveMemberRequestDto);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "회원 조회", description = "전체 회원 정보를 조회하여 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = FindMemberResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Description("Get All Member")
    @GetMapping("/members")
    public ResponseEntity<List<FindMemberResponseDto>> getMembers() {
        return ResponseEntity.ok(memberService.getMembers());
    }


    @Operation(summary = "Email로 회원 조회", description = "Email을 기반으로 회원 정보를 조회하여 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = FindMemberResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Description("Get Member By Email")
    @GetMapping("/member/{email}")
    public ResponseEntity<FindMemberResponseDto> getMemberByEmail(@PathVariable String email) {
        return ResponseEntity.ok(memberService.getMemberByEmail(email));
    }


    @Operation(summary = "Email로 회원 삭제", description = "Email을 기반으로 회원 정보를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Description("Delete Member By Email")
    @DeleteMapping("/member/{email}")
    public ResponseEntity<?> deleteMemberByEmail(@PathVariable String email) {
        memberService.deleteMemberByEmail(email);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Email로 회원 수정", description = "Email을 기반으로 회원 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Description("Update Member By Email")
    @PutMapping("/member/{email}")
    public ResponseEntity<?> updateMemberByEmail(@PathVariable String email, @RequestBody UpdateMemberRequestDto updateMemberRequestDto) {
        memberService.updateMemberByEmail(email, updateMemberRequestDto);
        return ResponseEntity.ok().build();
    }
}
