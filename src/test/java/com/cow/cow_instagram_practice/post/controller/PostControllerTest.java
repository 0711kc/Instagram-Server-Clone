package com.cow.cow_instagram_practice.post.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import com.cow.cow_instagram_practice.config.WithAuthUser;
import com.cow.cow_instagram_practice.image.entity.PostImage;
import com.cow.cow_instagram_practice.image.service.ImageServiceImpl;
import com.cow.cow_instagram_practice.jwt.JWTUtil;
import com.cow.cow_instagram_practice.member.entity.Member;
import com.cow.cow_instagram_practice.member.service.MemberServiceImpl;
import com.cow.cow_instagram_practice.post.controller.dto.request.PostRequest;
import com.cow.cow_instagram_practice.post.controller.dto.request.UpdatePostRequest;
import com.cow.cow_instagram_practice.post.entity.Post;
import com.cow.cow_instagram_practice.post.service.PostServiceImpl;

@WebMvcTest(PostController.class)
@Import(JWTUtil.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostControllerTest {
	@Autowired
	MockMvc mockMvc;

	@MockBean
	PostServiceImpl postService;

	@MockBean
	ImageServiceImpl imageService;

	@MockBean
	MemberServiceImpl memberService;

	@Test
	@DisplayName("게시글 등록 테스트")
	@WithAuthUser(id = "test1234", role = "ROLE_ADMIN")
	void createPostTest() throws Exception {
		Member member = Member.builder().id("test1234").name("Test User").build();
		PostImage postImage = PostImage.builder().id(1L).imageLink("test Link").build();
		MockMultipartFile imageFile = new MockMultipartFile(
			"image",
			"profile.png",
			MediaType.IMAGE_PNG_VALUE,
			"profile".getBytes()
		);
		PostRequest postRequest = PostRequest.builder().content("Test Post").build();
		String json = new ObjectMapper().writeValueAsString(postRequest);
		MockMultipartFile requestFile = new MockMultipartFile("request", "",
			"application/json", json.getBytes(StandardCharsets.UTF_8));
		given(imageService.uploadPostImage(imageFile)).willReturn(postImage);
		given(memberService.findOne(member.getId())).willReturn(member);
		given(postService.create(any(), any(), any())).willReturn(
			ResponseEntity.status(HttpStatus.CREATED).build()
		);

		mockMvc.perform(
				multipart(HttpMethod.POST, "/post/new")
					.file(imageFile)
					.file(requestFile)
					.contentType("multipart/form-data")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andDo(print());

		verify(imageService).uploadPostImage(imageFile);
		verify(memberService).findOne(member.getId());
		verify(postService).create(any(), any(), any());
	}

	@Test
	@DisplayName("게시글 조회 테스트")
	@WithMockUser
	void getPostTest() throws Exception {
		Long postId = 1L;
		Member member = Member.builder().id("test1234").name("Test User").build();
		PostImage postImage = PostImage.builder().imageLink("test Link").build();
		given(postService.findOne(postId)).willReturn(
			Post.builder()
				.id(postId)
				.content("Test Post")
				.postImage(postImage)
				.member(member)
				.build()
		);

		mockMvc.perform(
				get("/post/" + postId))
			.andExpect(status().isOk())
			.andDo(print());

		verify(postService).findOne(postId);
	}

	@Test
	@DisplayName("게시글 수정 테스트")
	@WithMockUser
	void updatePostTest() throws Exception {
		long postId = 1L;
		UpdatePostRequest updatePostRequest = UpdatePostRequest.builder().content("Update Content").build();
		String json = new ObjectMapper().writeValueAsString(updatePostRequest);
		given(postService.update(anyLong(), any(UpdatePostRequest.class))).willReturn(
			ResponseEntity.status(HttpStatus.OK).build()
		);

		mockMvc.perform(
				patch("/post/" + postId)
					.content(json)
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());

		verify(postService).update(any(), any(UpdatePostRequest.class));
	}

	@Test
	@DisplayName("게시글 이미지 수정 테스트")
	@WithMockUser
	void updateImagePostTest() throws Exception {
		long postId = 1L;
		MockMultipartFile imageFile = new MockMultipartFile(
			"image",
			"profile.png",
			MediaType.IMAGE_PNG_VALUE,
			"profile".getBytes()
		);
		PostImage postImage = PostImage.builder().build();
		given(imageService.uploadPostImage(imageFile)).willReturn(postImage);
		given(postService.update(anyLong(), any(PostImage.class))).willReturn(
			ResponseEntity.status(HttpStatus.OK).build()
		);

		mockMvc.perform(
				multipart(HttpMethod.PATCH, "/post/" + postId + "/image")
					.file(imageFile))
			.andExpect(status().isOk())
			.andDo(print());

		verify(imageService).uploadPostImage(imageFile);
		verify(postService).update(anyLong(), any(PostImage.class));
	}

	@Test
	@DisplayName("내 게시글 조회 테스트")
	@WithAuthUser(id = "0711kc", role = "ROLE_ADMIN")
	void getMyPostsTest() throws Exception {
		String memberId = "0711kc";
		Member member = Member.builder().id(memberId).role("ROLE_ADMIN").build();
		given(memberService.findOne(memberId)).willReturn(member);
		given(postService.findAllByMember(member)).willReturn(
			ResponseEntity.status(HttpStatus.OK).build()
		);

		mockMvc.perform(
				get("/post/all/my"))
			.andExpect(status().isOk())
			.andDo(print());

		verify(postService).findAllByMember(member);
	}

	@Test
	@DisplayName("모든 게시글 조회 테스트")
	@WithMockUser
	void getAllPostsTest() throws Exception {
		Long cursor = 0L;
		given(postService.findAll(cursor)).willReturn(
			ResponseEntity.status(HttpStatus.OK).build()
		);

		mockMvc.perform(
				get("/post/all")
					.param("cursor", cursor.toString()))
			.andExpect(status().isOk())
			.andDo(print());

		verify(postService).findAll(cursor);
	}

	@Test
	@DisplayName("피드 조회 테스트")
	@WithAuthUser(id = "0711kc", role = "ROLE_ADMIN")
	void getFeedTest() throws Exception {
		String memberId = "0711kc";
		Long cursor = 0L;
		given(postService.getFeed(memberId, cursor)).willReturn(
			ResponseEntity.status(HttpStatus.OK).build()
		);

		mockMvc.perform(
				get("/post/feed")
					.param("cursor", cursor.toString()))
			.andExpect(status().isOk())
			.andDo(print());

		verify(postService).getFeed(memberId, cursor);
	}

	@Test
	@DisplayName("게시글 삭제 테스트")
	@WithMockUser
	void deletePostTest() throws Exception {
		Long postId = 1L;
		given(postService.delete(postId)).willReturn(
			ResponseEntity.status(HttpStatus.NO_CONTENT).build()
		);

		mockMvc.perform(
				delete("/post/" + postId))
			.andExpect(status().isNoContent())
			.andDo(print());

		verify(postService).delete(postId);
	}
}
