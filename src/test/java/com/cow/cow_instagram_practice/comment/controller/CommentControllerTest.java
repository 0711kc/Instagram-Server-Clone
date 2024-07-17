package com.cow.cow_instagram_practice.comment.controller;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import com.cow.cow_instagram_practice.comment.controller.dto.request.CommentRequest;
import com.cow.cow_instagram_practice.comment.controller.dto.request.UpdateCommentRequest;
import com.cow.cow_instagram_practice.comment.service.CommentService;
import com.cow.cow_instagram_practice.config.WithAuthUser;
import com.cow.cow_instagram_practice.jwt.JWTUtil;
import com.cow.cow_instagram_practice.member.entity.Member;
import com.cow.cow_instagram_practice.member.service.MemberService;
import com.cow.cow_instagram_practice.post.entity.Post;
import com.cow.cow_instagram_practice.post.service.PostService;

@WebMvcTest(CommentController.class)
@Import(JWTUtil.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommentControllerTest {
	@Autowired
	MockMvc mockMvc;

	@MockBean
	CommentService commentService;

	@MockBean
	MemberService memberService;

	@MockBean
	PostService postService;

	@Test
	@DisplayName("댓글 생성 테스트")
	@WithAuthUser(id = "0711kc", role = "ROLE_ADMIN")
	void createComment() throws Exception {
		String memberId = "0711kc";
		Long postId = 1L;
		Member member = Member.builder().id(memberId).build();
		Post post = Post.builder().id(postId).member(member).build();
		CommentRequest commentRequest = CommentRequest.builder().content("Comment's Content").build();
		String json = new ObjectMapper().writeValueAsString(commentRequest);
		given(memberService.findOne(memberId)).willReturn(member);
		given(postService.findOne(postId)).willReturn(post);
		given(commentService.create(any(Member.class), any(Post.class), any(CommentRequest.class))).willReturn(
			ResponseEntity.status(HttpStatus.OK).build()
		);

		mockMvc.perform(
				post("/comment/new/" + postId)
					.content(json)
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		verify(commentService).create(any(Member.class), any(Post.class), any(CommentRequest.class));
	}

	@Test
	@DisplayName("답글 생성 테스트")
	@WithAuthUser(id = "0711kc", role = "ROLE_ADMIN")
	void createReply() throws Exception {
		String memberId = "0711kc";
		Long postId = 1L;
		Long commentId = 1L;
		Member member = Member.builder().id(memberId).build();
		Post post = Post.builder().id(postId).member(member).build();
		CommentRequest commentRequest = CommentRequest.builder().content("Comment's Content").build();
		String json = new ObjectMapper().writeValueAsString(commentRequest);
		given(memberService.findOne(memberId)).willReturn(member);
		given(postService.findOne(postId)).willReturn(post);
		given(commentService.createReply(any(Member.class), any(Post.class), anyLong(),
			any(CommentRequest.class))).willReturn(
			ResponseEntity.status(HttpStatus.OK).build()
		);

		mockMvc.perform(
				post("/comment/new/" + postId + "/" + commentId)
					.content(json)
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		verify(commentService).createReply(any(Member.class), any(Post.class), anyLong(), any(CommentRequest.class));
	}

	@Test
	@DisplayName("댓글 조회 테스트")
	@WithAuthUser(id = "0711kc", role = "ROLE_ADMIN")
	void getComment() throws Exception {
		Long commentId = 1L;
		given(commentService.findOne(commentId)).willReturn(
			ResponseEntity.status(HttpStatus.OK).build()
		);

		mockMvc.perform(
				get("/comment/" + commentId))
			.andExpect(status().isOk())
			.andDo(print());

		verify(commentService).findOne(commentId);
	}

	@Test
	@DisplayName("특정 회원의 댓글 조회 테스트")
	@WithAuthUser(id = "0711kc", role = "ROLE_ADMIN")
	void getCommentsByMember() throws Exception {
		String memberId = "0711kc";
		given(commentService.findByMember(memberId)).willReturn(
			ResponseEntity.status(HttpStatus.OK).build()
		);

		mockMvc.perform(
				get("/comment"))
			.andExpect(status().isOk())
			.andDo(print());

		verify(commentService).findByMember(memberId);
	}

	@Test
	@DisplayName("특정 게시글의 댓글 조회 테스트")
	@WithAuthUser(id = "0711kc", role = "ROLE_ADMIN")
	void getCommentsByPost() throws Exception {
		Long postId = 1L;
		given(commentService.findByPost(postId)).willReturn(
			ResponseEntity.status(HttpStatus.OK).build()
		);

		mockMvc.perform(
				get("/comment/post/" + postId))
			.andExpect(status().isOk())
			.andDo(print());

		verify(commentService).findByPost(postId);
	}

	@Test
	@DisplayName("특정 댓글의 답글 조회 테스트")
	@WithAuthUser(id = "0711kc", role = "ROLE_ADMIN")
	void getReplies() throws Exception {
		Long commentId = 1L;
		given(commentService.findReplies(commentId)).willReturn(
			ResponseEntity.status(HttpStatus.OK).build()
		);

		mockMvc.perform(
				get("/comment/reply/" + commentId))
			.andExpect(status().isOk())
			.andDo(print());

		verify(commentService).findReplies(commentId);
	}

	@Test
	@DisplayName("댓글 수정 테스트")
	@WithAuthUser(id = "0711kc", role = "ROLE_ADMIN")
	void updateComment() throws Exception {
		Long commentId = 1L;
		UpdateCommentRequest updateCommentRequest = UpdateCommentRequest.builder().content("Comment's Content").build();
		String json = new ObjectMapper().writeValueAsString(updateCommentRequest);
		given(commentService.update(anyLong(), any(UpdateCommentRequest.class))).willReturn(
			ResponseEntity.status(HttpStatus.OK).build()
		);

		mockMvc.perform(
				patch("/comment/" + commentId)
					.content(json)
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());

		verify(commentService).update(anyLong(), any(UpdateCommentRequest.class));
	}

	@Test
	@DisplayName("댓글 삭제 테스트")
	@WithAuthUser(id = "0711kc", role = "ROLE_ADMIN")
	void deleteComment() throws Exception {
		Long commentId = 1L;
		given(commentService.delete(commentId)).willReturn(
			ResponseEntity.status(HttpStatus.NO_CONTENT).build()
		);

		mockMvc.perform(
				delete("/comment/" + commentId))
			.andExpect(status().isNoContent())
			.andDo(print());

		verify(commentService).delete(commentId);
	}
}
