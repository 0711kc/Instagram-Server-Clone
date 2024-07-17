package com.cow.cow_instagram_practice.member.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cow.cow_instagram_practice.image.entity.ProfileImage;
import com.cow.cow_instagram_practice.image.repository.ProfileImageRepository;
import com.cow.cow_instagram_practice.member.controller.dto.request.MemberRequest;
import com.cow.cow_instagram_practice.member.controller.dto.request.MemberRole;
import com.cow.cow_instagram_practice.member.controller.dto.request.UpdateMemberRequest;
import com.cow.cow_instagram_practice.member.controller.dto.response.MemberResponse;
import com.cow.cow_instagram_practice.member.entity.Member;
import com.cow.cow_instagram_practice.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
	@InjectMocks
	MemberServiceImpl memberService;

	@Mock
	MemberRepository memberRepository;

	@Mock
	ProfileImageRepository profileImageRepository;

	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;

	private Member setUpMember(String id) {
		String password = "1234qwe!";
		String name = "Test";
		String nickname = "test123";
		String phone = "010-1234-5678";
		String email = "test@gmail.com";
		MemberRole memberRole = MemberRole.Admin;
		String imageLink = "https://mycowpracticebucket.s3.ap-northeast-2.amazonaws.com/anonymous.png";
		ProfileImage profileImage = ProfileImage.builder().id(1L).imageLink(imageLink).build();
		return Member.builder().id(id).password(password).name(name).nickname(nickname).phone(phone)
			.email(email).profileImage(profileImage).role(memberRole.getPermission()).build();
	}

	@Test
	@DisplayName("회원 불러오기")
	public void getMember() {
		String memberId = "test123";
		Member member = setUpMember(memberId);
		given(memberRepository.findByIdJoinFetch(memberId)).willReturn(Optional.of(member));
		Member findMember = memberService.findOne(memberId);

		checkMember(findMember);
	}

	@Test
	@DisplayName("회원 전체 불러오기")
	public void getAllMember() {
		Member member1 = setUpMember("test123");
		Member member2 = setUpMember("0711kc");
		List<Member> members = Arrays.asList(member1, member2);
		given(memberRepository.findAll()).willReturn(members);
		ResponseEntity<List<MemberResponse>> memberResponseEntity = memberService.findAll();

		HttpStatusCode status = memberResponseEntity.getStatusCode();
		List<MemberResponse> memberResponses = memberResponseEntity.getBody();
		Assertions.assertThat(status).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(memberResponses).isNotNull();
		Assertions.assertThat(memberResponses.size()).isEqualTo(2);
		checkMember(memberResponses.get(0), member1);
		checkMember(memberResponses.get(1), member2);
	}

	@Test
	@DisplayName("회원 저장")
	public void saveMember() {
		String id = "0711kc";
		String password = "1234qwe!";
		String name = "Kim Chan";
		String nickname = "kc123";
		String phone = "010-9876-5432";
		String email = "123qwe@gmail.com";
		MemberRole role = MemberRole.Admin;
		MemberRequest memberRequest = MemberRequest.builder()
			.id(id).password(password).name(name).nickname(nickname).phone(phone).email(email).role(role).build();
		String imageLink = "https://mycowpracticebucket.s3.ap-northeast-2.amazonaws.com/anonymous.png";
		ProfileImage profileImage = ProfileImage.builder().id(1L).imageLink(imageLink).build();
		Member member = memberRequest.toEntity(profileImage);
		given(memberRepository.save(any())).willReturn(member);
		given(profileImageRepository.findById(1L)).willReturn(Optional.of(profileImage));

		ResponseEntity<MemberResponse> memberResponseEntity = memberService.join(memberRequest);

		HttpStatusCode status = memberResponseEntity.getStatusCode();
		MemberResponse memberResponse = memberResponseEntity.getBody();
		Assertions.assertThat(status).isEqualTo(HttpStatus.CREATED);
		checkMember(memberResponse, member);
	}

	@Test
	@DisplayName("회원 삭제")
	public void deleteMember() {
		String memberId = "test123";
		given(memberRepository.existsById(memberId)).willReturn(true);

		ResponseEntity<Void> responseEntity = memberService.delete(memberId);

		HttpStatusCode status = responseEntity.getStatusCode();
		Assertions.assertThat(status).isEqualTo(HttpStatus.NO_CONTENT);
	}

	@Test
	@DisplayName("회원 수정")
	public void updateMember() {
		String memberId = "test123";
		Member member = setUpMember(memberId);
		given(memberRepository.findByIdJoinFetch(memberId)).willReturn(Optional.of(member));
		UpdateMemberRequest updateMemberRequest = UpdateMemberRequest.builder()
			.name("Lee Chan").phone("010-9876-5432").build();

		MemberResponse beforeMemberResponse = MemberResponse.from(memberService.findOne(memberId));
		ResponseEntity<MemberResponse> afterResponseEntity = memberService.updateById(memberId, updateMemberRequest);

		HttpStatusCode status = afterResponseEntity.getStatusCode();
		MemberResponse afterMemberResponse = afterResponseEntity.getBody();
		Assertions.assertThat(status).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(beforeMemberResponse).isNotNull();
		Assertions.assertThat(afterMemberResponse).isNotNull();
		Assertions.assertThat(beforeMemberResponse.getName()).isNotEqualTo(afterMemberResponse.getName());
		Assertions.assertThat(beforeMemberResponse.getPhone()).isNotEqualTo(afterMemberResponse.getPhone());
		Assertions.assertThat(beforeMemberResponse.getNickname()).isEqualTo(afterMemberResponse.getNickname());
		Assertions.assertThat(beforeMemberResponse.getEmail()).isEqualTo(afterMemberResponse.getEmail());
	}

	@Test
	@DisplayName("회원 프로필 이미지 수정")
	public void updateImageMember() {
		String memberId = "test123";
		Member member = setUpMember(memberId);
		given(memberRepository.findByIdJoinFetch(memberId)).willReturn(Optional.of(member));
		ProfileImage profileImage = ProfileImage.builder().id(2L).imageLink("testLink").build();

		MemberResponse beforeMemberResponse = MemberResponse.from(memberService.findOne(memberId));
		ResponseEntity<MemberResponse> afterResponseEntity = memberService.updateImageById(memberId, profileImage);

		HttpStatusCode status = afterResponseEntity.getStatusCode();
		MemberResponse afterMemberResponse = afterResponseEntity.getBody();
		Assertions.assertThat(status).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(beforeMemberResponse).isNotNull();
		Assertions.assertThat(afterMemberResponse).isNotNull();
		Assertions.assertThat(beforeMemberResponse.getImage()).isNotEqualTo(afterMemberResponse.getImage());
		Assertions.assertThat(beforeMemberResponse.getNickname()).isEqualTo(afterMemberResponse.getNickname());
	}

	private void checkMember(Member member) {
		Assertions.assertThat(member).isNotNull();
		Assertions.assertThat(member.getId()).isEqualTo("test123");
		Assertions.assertThat(member.getName()).isEqualTo("Test");
		Assertions.assertThat(member.getNickname()).isEqualTo("test123");
		Assertions.assertThat(member.getPhone()).isEqualTo("010-1234-5678");
		Assertions.assertThat(member.getEmail()).isEqualTo("test@gmail.com");
		Assertions.assertThat(member.getProfileImage().getImageLink())
			.isEqualTo("https://mycowpracticebucket.s3.ap-northeast-2.amazonaws.com/anonymous.png");
		Assertions.assertThat(member.getRole()).isEqualTo("ROLE_ADMIN");
	}

	private void checkMember(MemberResponse memberResponse, Member member) {
		Assertions.assertThat(memberResponse).isNotNull();
		Assertions.assertThat(memberResponse.getId()).isEqualTo(member.getId());
		Assertions.assertThat(memberResponse.getName()).isEqualTo(member.getName());
		Assertions.assertThat(memberResponse.getNickname()).isEqualTo(member.getNickname());
		Assertions.assertThat(memberResponse.getPhone()).isEqualTo(member.getPhone());
		Assertions.assertThat(memberResponse.getEmail()).isEqualTo(member.getEmail());
		Assertions.assertThat(memberResponse.getImage()).isEqualTo(member.getProfileImage().getImageLink());
		Assertions.assertThat(memberResponse.getRole()).isEqualTo(member.getRole());
	}
}
