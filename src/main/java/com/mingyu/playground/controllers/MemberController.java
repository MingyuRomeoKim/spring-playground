package com.mingyu.playground.controllers;

import com.mingyu.playground.dto.request.SaveMemberRequestDto;
import com.mingyu.playground.dto.request.UpdateMemberRequestDto;
import com.mingyu.playground.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MemberController {

    private final MemberService memberService;

    @Description("Save Member")
    @PostMapping("/member")
    public ResponseEntity<?> saveMember(@RequestBody SaveMemberRequestDto saveMemberRequestDto) {
        memberService.saveMember(saveMemberRequestDto);
        return ResponseEntity.ok().build();
    }

    @Description("Get All Member")
    @GetMapping("/members")
    public ResponseEntity<?> getMembers() {
        return ResponseEntity.ok(memberService.getMembers());
    }

    @Description("Get Member By Email")
    @GetMapping("/member/{email}")
    public ResponseEntity<?> getMemberByEmail(@PathVariable String email) {
        return ResponseEntity.ok(memberService.getMemberByEmail(email));
    }

    @Description("Delete Member By Email")
    @DeleteMapping("/member/{email}")
    public ResponseEntity<?> deleteMemberByEmail(@PathVariable String email) {
        memberService.deleteMemberByEmail(email);
        return ResponseEntity.ok().build();
    }

    @Description("Update Member By Email")
    @PutMapping("/member/{email}")
    public ResponseEntity<?> updateMemberByEmail(@PathVariable String email, @RequestBody UpdateMemberRequestDto updateMemberRequestDto) {
        memberService.updateMemberByEmail(email, updateMemberRequestDto);
        return ResponseEntity.ok().build();
    }
}
