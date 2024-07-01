package com.cow.cow_instagram_practice.member.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

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
}
