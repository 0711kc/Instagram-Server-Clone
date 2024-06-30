package com.cow.cow_instagram_practice.member.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cow.cow_instagram_practice.member.controller.dto.request.MemberRequest;
import com.cow.cow_instagram_practice.member.controller.dto.response.MemberResponse;
import com.cow.cow_instagram_practice.member.entity.ProfileImage;

public interface MemberService {
	ResponseEntity<MemberResponse> findOne(String memberId);

	ResponseEntity<MemberResponse> join(MemberRequest memberRequest);

	ResponseEntity<List<MemberResponse>> findAll();

	ResponseEntity<Void> delete(String memberId);

	// ResponseEntity<MemberResponse> updateById(Long memberId, UpdateMemberRequest updateMemberRequest);
}
