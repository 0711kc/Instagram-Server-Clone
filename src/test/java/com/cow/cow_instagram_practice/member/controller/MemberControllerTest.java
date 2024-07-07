package com.cow.cow_instagram_practice.member.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import com.cow.cow_instagram_practice.image.service.ImageServiceImpl;
import com.cow.cow_instagram_practice.jwt.JWTUtil;
import com.cow.cow_instagram_practice.member.controller.dto.request.MemberRequest;
import com.cow.cow_instagram_practice.member.controller.dto.request.MemberRole;
import com.cow.cow_instagram_practice.member.controller.dto.request.UpdateMemberRequest;
import com.cow.cow_instagram_practice.image.entity.ProfileImage;
import com.cow.cow_instagram_practice.member.service.MemberServiceImpl;

@WebMvcTest(MemberController.class)
@Import(JWTUtil.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MemberControllerTest {
	@Autowired
	MockMvc mockMvc;

	@MockBean
	MemberServiceImpl memberService;

	@MockBean
	ImageServiceImpl imageService;

	@Autowired
	WebApplicationContext context;

	@Test
	@DisplayName("회원 조회 테스트")
	@WithAnonymousUser
	void getMemberTest() throws Exception {
		String memberId = "0711kc";

		given(memberService.findOne(memberId)).willReturn(
			ResponseEntity.ok().build()
		);

		mockMvc.perform(
				get("/member/" + memberId))
			.andExpect(status().isOk())
			.andDo(print());

		verify(memberService).findOne(memberId);
	}

	@Test
	@DisplayName("회원 등록 테스트")
	@WithAnonymousUser
	void registerMemberTest() throws Exception {
		String memberId = "0711kc";
		MemberRequest memberRequest = MemberRequest.builder()
			.id(memberId).password("1234qwe!").name("Kim Chan").nickname("KC1234")
			.phone("010-1234-5678").email("123asd@gmail.com").role(MemberRole.Admin)
			.build();
		String json = new ObjectMapper().writeValueAsString(memberRequest);

		given(memberService.join(any())).willReturn(
			ResponseEntity.status(HttpStatus.CREATED).build()
		);

		mockMvc.perform(
				post("/member/new")
					.content(json)
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andDo(print());

		verify(memberService).join(any());
	}

	@Test
	@DisplayName("회원 삭제 테스트")
	@WithMockUser
	void deleteMemberTest() throws Exception {
		String memberId = "0711kc";

		given(memberService.delete(memberId)).willReturn(
			ResponseEntity.noContent().build()
		);

		mockMvc.perform(
				delete("/member/" + memberId))
			.andExpect(status().isNoContent())
			.andDo(print());

		verify(memberService).delete(memberId);
	}

	@Test
	@DisplayName("회원 수정 테스트")
	@WithMockUser
	void updateMemberTest() throws Exception {
		String memberId = "0711kc";

		UpdateMemberRequest updateMemberRequest = UpdateMemberRequest.builder().name("Lee Chan").build();
		String json = new ObjectMapper().writeValueAsString(updateMemberRequest);

		given(memberService.updateById(memberId, updateMemberRequest)).willReturn(
			ResponseEntity.ok().build()
		);

		mockMvc.perform(
				patch("/member/" + memberId)
					.content(json)
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());

		verify(memberService).updateById(anyString(), any());
	}

	@Test
	@DisplayName("회원 프로필 수정 테스트")
	@WithMockUser
	void updateImageMemberTest() throws Exception {
		String memberId = "0711kc";
		MockMultipartFile imageFile = new MockMultipartFile(
			"image",
			"profile.png",
			MediaType.IMAGE_PNG_VALUE,
			"profile".getBytes()
		);
		ProfileImage profileImage = ProfileImage.builder().id(2L).imageLink("profile.png").build();
		given(imageService.upload(imageFile)).willReturn(profileImage);
		given(memberService.updateImageById(memberId, profileImage)).willReturn(
			ResponseEntity.ok().build()
		);

		mockMvc.perform(
				multipart(HttpMethod.PATCH, "/member/" + memberId + "/image")
					.file(imageFile))
			.andExpect(status().isOk())
			.andDo(print());

		verify(memberService).updateImageById(anyString(), any());
	}
}
