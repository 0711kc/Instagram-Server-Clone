package com.cow.cow_instagram_practice.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cow.cow_instagram_practice.member.controller.dto.request.MemberRequest;
import com.cow.cow_instagram_practice.member.controller.dto.request.UpdateMemberRequest;
import com.cow.cow_instagram_practice.member.controller.dto.response.MemberResponse;
import com.cow.cow_instagram_practice.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;

	@PostMapping("/new")
	public ResponseEntity<MemberResponse> create(@RequestBody @Valid final MemberRequest memberRequest) {
		return memberService.join(memberRequest);
	}

	@GetMapping("/{memberId}")
	public ResponseEntity<MemberResponse> findMember(@PathVariable final String memberId) {
		return memberService.findOne(memberId);
	}

	@DeleteMapping("/{memberId}")
	public ResponseEntity<Void> delete(@PathVariable final String memberId) {
		return memberService.delete(memberId);
	}

	@PatchMapping("/{memberId}")
	public ResponseEntity<MemberResponse> update(@PathVariable final String memberId,
		@RequestBody @Valid UpdateMemberRequest updateMemberRequest) {
		return memberService.updateById(memberId, updateMemberRequest);
	}
}
