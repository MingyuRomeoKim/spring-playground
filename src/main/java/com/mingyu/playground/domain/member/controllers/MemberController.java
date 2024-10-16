package com.mingyu.playground.domain.member.controllers;

import com.mingyu.playground.common.response.PlayGroundResponse;
import com.mingyu.playground.domain.member.dto.request.SaveMemberRequestDto;
import com.mingyu.playground.domain.member.dto.request.UpdateMemberRequestDto;
import com.mingyu.playground.domain.member.dto.response.FindMemberResponseDto;
import com.mingyu.playground.domain.member.services.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Tag(name = "Member", description = "Member 관련 Controller 입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 등록", description = "회원 정보가 등록됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Description("Save Member")
    @PostMapping("")
    public ResponseEntity<?> saveMember(@Valid @RequestBody SaveMemberRequestDto saveMemberRequestDto) {
        memberService.saveMember(saveMemberRequestDto);
        return PlayGroundResponse.ok();
    }


    @Operation(summary = "회원 조회", description = "전체 회원 정보를 조회하여 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = FindMemberResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Description("Get All Member")
    @GetMapping("/list")
    public ResponseEntity<?> getMembers() {
        return PlayGroundResponse.build(memberService.getMembers());
    }


    @Operation(summary = "Email로 회원 조회", description = "Email을 기반으로 회원 정보를 조회하여 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = FindMemberResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Not Allowed Request - 올바르지 않는 요청입니다."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Description("Get Member By Email")
    @GetMapping("/{email}")
    public ResponseEntity<?> getMemberByEmail(@Email @PathVariable("email") String email) {
        return PlayGroundResponse.build(memberService.getMemberByEmail(email));
    }


    @Operation(summary = "Email로 회원 삭제", description = "Email을 기반으로 회원 정보를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Description("Delete Member By Email")
    @DeleteMapping("/{email}")
    public ResponseEntity<?> deleteMemberByEmail(@Email @PathVariable("email") String email) {
        memberService.deleteMemberByEmail(email);
        return PlayGroundResponse.ok();
    }


    @Operation(summary = "Email로 회원 수정", description = "Email을 기반으로 회원 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Description("Update Member By Email")
    @PutMapping("/{email}")
    public ResponseEntity<?> updateMemberByEmail(@Email @PathVariable("email") String email, @Valid @RequestBody UpdateMemberRequestDto updateMemberRequestDto) {
        memberService.updateMemberByEmail(email, updateMemberRequestDto);
        return PlayGroundResponse.ok();
    }
}
