package com.cow.cow_instagram_practice.member.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cow.cow_instagram_practice.member.controller.dto.request.MemberRequest;
import com.cow.cow_instagram_practice.member.controller.dto.response.MemberResponse;
import com.cow.cow_instagram_practice.member.entity.Member;
import com.cow.cow_instagram_practice.member.entity.ProfileImage;
import com.cow.cow_instagram_practice.member.repository.MemberRepository;
import com.cow.cow_instagram_practice.member.repository.ProfileImageRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepository;
	private final ProfileImageRepository profileImageRepository;

	@Override
	public ResponseEntity<MemberResponse> join(MemberRequest memberRequest) {
		boolean isExistMember = memberRepository.existsById(memberRequest.getId());
		if (isExistMember) {
			throw new IllegalArgumentException("[Error] 이미 존재하는 아이디입니다.");
		}
		ProfileImage defaultImage = profileImageRepository.findById(1L)
			.orElseThrow(() -> new IllegalStateException("[Error] 기본 프로필 이미지에 접근할 수 없습니다."));
		Member member = memberRequest.toEntity(defaultImage);
		Member savedMember = memberRepository.save(member);
		return ResponseEntity.status(HttpStatus.CREATED)
			.contentType(MediaType.APPLICATION_JSON)
			.body(MemberResponse.from(savedMember));
	}

	@Transactional(readOnly = true)
	@Override
	public ResponseEntity<MemberResponse> findOne(String memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException("[Error] 사용자를 찾을 수 없습니다."));
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(MemberResponse.from(member));
	}

	@Transactional(readOnly = true)
	@Override
	public ResponseEntity<List<MemberResponse>> findAll() {
		return null;
	}

	@Override
	public ResponseEntity<Void> delete(String memberId) {
		return null;
	}
}
