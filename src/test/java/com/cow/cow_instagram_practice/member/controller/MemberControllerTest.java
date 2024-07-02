package com.cow.cow_instagram_practice.member.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import com.cow.cow_instagram_practice.member.controller.dto.request.MemberRequest;
import com.cow.cow_instagram_practice.member.controller.dto.request.MemberRole;
import com.cow.cow_instagram_practice.member.service.MemberServiceImpl;

@WebMvcTest(MemberController.class)
public class MemberControllerTest {
	@Autowired
	MockMvc mockMvc;

	@MockBean
	MemberServiceImpl memberService;

	@Test
	@DisplayName("회원 조회 테스트")
	void getMemberTest() throws Exception {
		String memberId = "0711kc";

		given(memberService.findOne(memberId)).willReturn(
			ResponseEntity.ok().build()
		);

		mockMvc.perform(
				get("/member/" + memberId))
			.andExpect(status().isOk());

		verify(memberService).findOne(memberId);
	}

	@Test
	@DisplayName("회원 등록 테스트")
	void newMemberTest() throws Exception {
		String memberId = "0711kc";
		MemberRequest memberRequest = MemberRequest.builder()
			.id(memberId).password("1234qwer!").name("Kim Chan").nickname("KC1234")
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
			.andExpect(status().isCreated());

		verify(memberService).join(any());
	}
}
