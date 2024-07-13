package com.cow.cow_instagram_practice.member.controller;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.util.Objects;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import com.cow.cow_instagram_practice.image.service.ImageServiceImpl;
import com.cow.cow_instagram_practice.jwt.JWTUtil;
import com.cow.cow_instagram_practice.member.controller.dto.request.MemberRequest;
import com.cow.cow_instagram_practice.member.controller.dto.request.MemberRole;
import com.cow.cow_instagram_practice.member.service.MemberServiceImpl;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(MemberController.class)
@Import(JWTUtil.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MemberAdviceTest {
	@Autowired
	MockMvc mockMvc;

	@MockBean
	MemberServiceImpl memberService;

	@MockBean
	ImageServiceImpl imageService;

	@Autowired
	WebApplicationContext context;

	@Test
	@DisplayName("존재하지 않는 회원 조회 테스트")
	@WithAnonymousUser
	void notExistMemberTest() throws Exception {
		String memberId = "0711kc";
		given(memberService.findOne(memberId))
			.willThrow(new EntityNotFoundException("[Error] 사용자를 찾을 수 없습니다."));

		mockMvc.perform(
				get("/member/" + memberId))
			.andExpect(status().isNotFound())
			.andExpect(result -> Assertions.assertThat(
				getApiResultExceptionClass(result)).isEqualTo(EntityNotFoundException.class))
			.andDo(print());

		verify(memberService).findOne(memberId);
	}

	@Test
	@DisplayName("존재하는 회원 등록 테스트")
	@WithAnonymousUser
	void alreadyExistMemberTest() throws Exception {
		String memberId = "0711kc";
		MemberRequest memberRequest = MemberRequest.builder()
			.id(memberId).password("1234qwe!").name("Kim Chan").nickname("KC1234")
			.phone("010-1234-5678").email("123asd@gmail.com").role(MemberRole.Admin)
			.build();
		String json = new ObjectMapper().writeValueAsString(memberRequest);
		given(memberService.join(any()))
			.willThrow(new IllegalArgumentException("[Error] 이미 존재하는 아이디입니다."));

		mockMvc.perform(
				post("/member/new")
					.content(json)
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(result -> Assertions.assertThat(
				getApiResultExceptionClass(result)).isEqualTo(IllegalArgumentException.class))
			.andDo(print());

		verify(memberService).join(any());
	}

	@Test
	@DisplayName("기본 프로필 이미지 접근 실패 테스트")
	@WithAnonymousUser
	void failedAccessProfileImageTest() throws Exception {
		String memberId = "0711kc";
		MemberRequest memberRequest = MemberRequest.builder()
			.id(memberId).password("1234qwe!").name("Kim Chan").nickname("KC1234")
			.phone("010-1234-5678").email("123asd@gmail.com").role(MemberRole.Admin)
			.build();
		String json = new ObjectMapper().writeValueAsString(memberRequest);
		given(memberService.join(any()))
			.willThrow(new IllegalStateException("[Error] 기본 프로필 이미지에 접근할 수 없습니다."));

		mockMvc.perform(
				post("/member/new")
					.content(json)
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isInternalServerError())
			.andExpect(result -> Assertions.assertThat(
				getApiResultExceptionClass(result)).isEqualTo(IllegalStateException.class))
			.andDo(print());

		verify(memberService).join(any());
	}

	@Test
	@DisplayName("잘못된 양식의 회원 등록 테스트")
	@WithAnonymousUser
	void badRequestJoinMemberTest() throws Exception {
		String memberId = "0711kc";
		MemberRequest memberRequest = MemberRequest.builder()
			.id(memberId).name("Kim Chan").nickname("KC1234")
			.build();
		String json = new ObjectMapper().writeValueAsString(memberRequest);

		mockMvc.perform(
				post("/member/new")
					.content(json)
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(result -> Assertions.assertThat(
					getApiResultExceptionClass(result)).isEqualTo(MethodArgumentNotValidException.class))
			.andDo(print());

		verifyNoInteractions(memberService);
	}

	@Test
	@DisplayName("이미지 파일 작업 실패 테스트")
	@WithAnonymousUser
	void failedImageFileTest() throws Exception {
		String memberId = "0711kc";
		MockMultipartFile imageFile = new MockMultipartFile(
			"image",
			"profile.png",
			MediaType.IMAGE_PNG_VALUE,
			"profile".getBytes()
		);
		given(imageService.uploadProfileImage(imageFile)).willThrow(new IOException());

		mockMvc.perform(
				multipart(HttpMethod.PATCH, "/member/" + memberId + "/image")
					.file(imageFile))
			.andExpect(status().isInternalServerError())
			.andDo(print());

		verify(imageService).uploadProfileImage(imageFile);
	}

	private Class<? extends Exception> getApiResultExceptionClass(MvcResult result) {
		return Objects.requireNonNull(result.getResolvedException()).getClass();
	}
}
