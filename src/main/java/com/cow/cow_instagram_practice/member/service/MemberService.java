package com.cow.cow_instagram_practice.member.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cow.cow_instagram_practice.member.controller.dto.request.MemberRequest;
import com.cow.cow_instagram_practice.member.controller.dto.response.MemberResponse;

public interface MemberService {
	ResponseEntity<MemberResponse> findOne(String memberId);

	ResponseEntity<MemberResponse> join(MemberRequest memberRequest);

	ResponseEntity<List<MemberResponse>> findAll();

	ResponseEntity<Void> delete(String memberId);
}
