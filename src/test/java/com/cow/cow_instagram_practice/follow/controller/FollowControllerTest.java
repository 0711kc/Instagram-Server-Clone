package com.cow.cow_instagram_practice.follow.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.cow.cow_instagram_practice.config.WithAuthUser;
import com.cow.cow_instagram_practice.follow.service.FollowService;
import com.cow.cow_instagram_practice.jwt.JWTUtil;

@WebMvcTest(FollowController.class)
@Import(JWTUtil.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FollowControllerTest {
	@Autowired
	MockMvc mockMvc;

	@MockBean
	FollowService followService;

	@Test
	@DisplayName("팔로우 테스트")
	@WithAuthUser(id = "0711kc", role = "ROLE_ADMIN")
	void followTest() throws Exception {
		String followerId = "0711kc";
		String followingId = "test1234";
		given(followService.create(followerId, followingId)).willReturn(
			ResponseEntity.status(HttpStatus.OK).build()
		);

		mockMvc.perform(
				post("/follow/" + followingId))
			.andExpect(status().isOk())
			.andDo(print());

		verify(followService).create(followerId, followingId);
	}

	@Test
	@DisplayName("언팔로우 테스트")
	@WithAuthUser(id = "0711kc", role = "ROLE_ADMIN")
	void unfollowTest() throws Exception {
		String followerId = "0711kc";
		String followingId = "test1234";
		given(followService.delete(followerId, followingId)).willReturn(
			ResponseEntity.status(HttpStatus.NO_CONTENT).build()
		);

		mockMvc.perform(
				post("/follow/unfollow/" + followingId))
			.andExpect(status().isNoContent())
			.andDo(print());

		verify(followService).delete(followerId, followingId);
	}
}
