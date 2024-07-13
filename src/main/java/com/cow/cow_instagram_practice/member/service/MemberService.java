package com.cow.cow_instagram_practice.member.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cow.cow_instagram_practice.member.controller.dto.request.MemberRequest;
import com.cow.cow_instagram_practice.member.controller.dto.request.UpdateMemberRequest;
import com.cow.cow_instagram_practice.member.controller.dto.response.MemberResponse;
import com.cow.cow_instagram_practice.image.entity.ProfileImage;
import com.cow.cow_instagram_practice.member.entity.Member;

public interface MemberService {
	Member findOne(String memberId);

	ResponseEntity<MemberResponse> join(MemberRequest memberRequest);

	ResponseEntity<List<MemberResponse>> findAll();

	ResponseEntity<Void> delete(String memberId);

	ResponseEntity<MemberResponse> updateById(String memberId, UpdateMemberRequest updateMemberRequest);

	ResponseEntity<MemberResponse> updateImageById(String memberId, ProfileImage profileImage);
}
