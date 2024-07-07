package com.cow.cow_instagram_practice.member.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

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

import com.cow.cow_instagram_practice.member.controller.dto.request.MemberRequest;
import com.cow.cow_instagram_practice.member.controller.dto.request.MemberRole;
import com.cow.cow_instagram_practice.member.controller.dto.request.UpdateMemberRequest;
import com.cow.cow_instagram_practice.member.controller.dto.response.MemberResponse;
import com.cow.cow_instagram_practice.member.entity.Member;
import com.cow.cow_instagram_practice.image.entity.ProfileImage;
import com.cow.cow_instagram_practice.member.repository.MemberRepository;
import com.cow.cow_instagram_practice.image.repository.ProfileImageRepository;

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

	private void setUpMember() {
		String id = "test123";
		String password = "1234qwe!";
		String name = "Test";
		String nickname = "test123";
		String phone = "010-1234-5678";
		String email = "test@gmail.com";
		MemberRole memberRole = MemberRole.Admin;
		String imageLink = "https://mycowpracticebucket.s3.ap-northeast-2.amazonaws.com/anonymous.png";
		ProfileImage profileImage = ProfileImage.builder().id(1L).imageLink(imageLink).build();
		Member member = Member.builder().id(id).password(password).name(name).nickname(nickname)
			.phone(phone).email(email).profileImage(profileImage).role(memberRole.getPermission()).build();
		given(memberRepository.findById(id)).willReturn(Optional.of(member));
	}

	@Test
	@DisplayName("회원 불러오기")
	public void getMember() {
		setUpMember();
		String memberId = "test123";
		ResponseEntity<MemberResponse> memberResponseEntity = memberService.findOne(memberId);

		HttpStatusCode status = memberResponseEntity.getStatusCode();
		MemberResponse memberResponse = memberResponseEntity.getBody();
		Assertions.assertThat(status).isEqualTo(HttpStatus.OK);
		checkMember(memberResponse);
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
		setUpMember();
		String memberId = "test123";
		UpdateMemberRequest updateMemberRequest = UpdateMemberRequest.builder()
				.name("Lee Chan").phone("010-9876-5432").build();

		ResponseEntity<MemberResponse> beforeResponseEntity = memberService.findOne(memberId);
		ResponseEntity<MemberResponse> afterResponseEntity = memberService.updateById(memberId, updateMemberRequest);

		HttpStatusCode status = afterResponseEntity.getStatusCode();
		MemberResponse beforeMemberResponse = beforeResponseEntity.getBody();
		MemberResponse afterMemberResponse = afterResponseEntity.getBody();
		Assertions.assertThat(status).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(beforeMemberResponse).isNotNull();
		Assertions.assertThat(afterMemberResponse).isNotNull();

		Assertions.assertThat(beforeMemberResponse.getName()).isNotEqualTo(afterMemberResponse.getName());
		Assertions.assertThat(beforeMemberResponse.getPhone()).isNotEqualTo(afterMemberResponse.getPhone());
		Assertions.assertThat(beforeMemberResponse.getNickname()).isEqualTo(afterMemberResponse.getNickname());
		Assertions.assertThat(beforeMemberResponse.getEmail()).isEqualTo(afterMemberResponse.getEmail());
	}

	private void checkMember(MemberResponse memberResponse) {
		Assertions.assertThat(memberResponse).isNotNull();
		Assertions.assertThat(memberResponse.getId()).isEqualTo("test123");
		Assertions.assertThat(memberResponse.getName()).isEqualTo("Test");
		Assertions.assertThat(memberResponse.getNickname()).isEqualTo("test123");
		Assertions.assertThat(memberResponse.getPhone()).isEqualTo("010-1234-5678");
		Assertions.assertThat(memberResponse.getEmail()).isEqualTo("test@gmail.com");
		Assertions.assertThat(memberResponse.getImage())
			.isEqualTo("https://mycowpracticebucket.s3.ap-northeast-2.amazonaws.com/anonymous.png");
		Assertions.assertThat(memberResponse.getRole()).isEqualTo("ROLE_ADMIN");
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
