package com.cow.cow_instagram_practice.member.service;

import java.beans.FeatureDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cow.cow_instagram_practice.follow.repository.FollowRepository;
import com.cow.cow_instagram_practice.image.entity.ProfileImage;
import com.cow.cow_instagram_practice.image.repository.ProfileImageRepository;
import com.cow.cow_instagram_practice.member.controller.dto.request.MemberRequest;
import com.cow.cow_instagram_practice.member.controller.dto.request.UpdateMemberRequest;
import com.cow.cow_instagram_practice.member.controller.dto.response.MemberResponse;
import com.cow.cow_instagram_practice.member.entity.Member;
import com.cow.cow_instagram_practice.member.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepository;
	private final ProfileImageRepository profileImageRepository;
	private final FollowRepository followRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public ResponseEntity<MemberResponse> join(MemberRequest memberRequest) {
		boolean isExistMember = memberRepository.existsById(memberRequest.getId());
		if (isExistMember) {
			throw new IllegalArgumentException("[Error] 이미 존재하는 아이디입니다.");
		}
		ProfileImage defaultImage = profileImageRepository.findById(ProfileImageRepository.DEFAULT_PROFILE_ID)
			.orElseThrow(() -> new IllegalStateException("[Error] 기본 프로필 이미지에 접근할 수 없습니다."));
		Member member = memberRequest.toEntity(defaultImage);
		member.updatePassword(bCryptPasswordEncoder.encode(memberRequest.getPassword()));
		Member savedMember = memberRepository.save(member);
		return ResponseEntity.status(HttpStatus.CREATED)
			.contentType(MediaType.APPLICATION_JSON)
			.body(MemberResponse.from(savedMember));
	}

	@Transactional(readOnly = true)
	@Override
	public Member findOne(String memberId) {
		return memberRepository.findByIdJoinFetch(memberId)
			.orElseThrow(() -> new EntityNotFoundException("[Error] 사용자를 찾을 수 없습니다."));
	}

	@Transactional(readOnly = true)
	@Override
	public ResponseEntity<List<MemberResponse>> findAll() {
		List<Member> members = memberRepository.findAll();
		List<MemberResponse> memberResponses = members.stream()
			.map(MemberResponse::from)
			.collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(memberResponses);
	}

	@Override
	public ResponseEntity<Void> delete(String memberId) {
		boolean isExistMember = memberRepository.existsById(memberId);
		if (!isExistMember) {
			throw new EntityNotFoundException("[Error] 사용자를 찾을 수 없습니다.");
		}
		memberRepository.deleteById(memberId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Override
	public ResponseEntity<MemberResponse> updateById(String memberId, UpdateMemberRequest updateMemberRequest) {
		Member member = memberRepository.findByIdJoinFetch(memberId)
			.orElseThrow(() -> new EntityNotFoundException("[Error] 사용자를 찾을 수 없습니다."));
		UpdateMemberRequest existMember = UpdateMemberRequest.from(member);
		copyNonNullProperties(updateMemberRequest, existMember);
		String name = existMember.getName();
		String nickname = existMember.getNickname();
		String phone = existMember.getPhone();
		String email = existMember.getEmail();
		member.update(name, nickname, phone, email);
		memberRepository.save(member);
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(MemberResponse.from(member));
	}

	@Override
	public ResponseEntity<MemberResponse> updateImageById(String memberId, ProfileImage profileImage) {
		Member member = memberRepository.findByIdJoinFetch(memberId)
			.orElseThrow(() -> new EntityNotFoundException("[Error] 사용자를 찾을 수 없습니다."));
		member.updateProfileImage(profileImage);
		memberRepository.save(member);
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(MemberResponse.from(member));
	}

	private static void copyNonNullProperties(Object src, Object target) {
		BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
	}

	private static String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = Arrays.stream(pds)
			.map(FeatureDescriptor::getName)
			.filter(name -> src.getPropertyValue(name) == null)
			.collect(Collectors.toSet());
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}
}
