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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;

	@PostMapping("/new")
	@Operation(summary = "등록", description = "신규 회원 등록")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "등록 성공",
			content = {@Content(schema = @Schema(implementation = MemberResponse.class))}),
		@ApiResponse(responseCode = "400", description = "잘못된 요청")
	})
	public ResponseEntity<MemberResponse> create(@RequestBody @Valid final MemberRequest memberRequest) {
		return memberService.join(memberRequest);
	}

	@GetMapping("/{memberId}")
	@Operation(summary = "조회", description = "기존 회원 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = {@Content(schema = @Schema(implementation = MemberResponse.class))}),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 회원",
			content = {@Content(schema = @Schema(hidden = true))})
	})
	public ResponseEntity<MemberResponse> findMember(@PathVariable final String memberId) {
		return memberService.findOne(memberId);
	}

	@DeleteMapping("/{memberId}")
	@Operation(summary = "삭제", description = "기존 회원 삭제")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "삭제 성공",
			content = {@Content(schema = @Schema(implementation = MemberResponse.class))}),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 회원",
			content = {@Content(schema = @Schema(hidden = true))})
	})
	public ResponseEntity<Void> delete(@PathVariable final String memberId) {
		return memberService.delete(memberId);
	}

	@PatchMapping("/{memberId}")
	@Operation(summary = "수정", description = "기존 회원 수정")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "수정 성공",
			content = {@Content(schema = @Schema(implementation = MemberResponse.class))}),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 회원",
			content = {@Content(schema = @Schema(hidden = true))})
	})
	public ResponseEntity<MemberResponse> update(@PathVariable final String memberId,
		@RequestBody @Valid UpdateMemberRequest updateMemberRequest) {
		return memberService.updateById(memberId, updateMemberRequest);
	}
}
