package com.cow.cow_instagram_practice.comment.service;

import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
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
import org.springframework.http.ResponseEntity;

import com.cow.cow_instagram_practice.comment.controller.dto.request.CommentRequest;
import com.cow.cow_instagram_practice.comment.controller.dto.request.UpdateCommentRequest;
import com.cow.cow_instagram_practice.comment.controller.dto.response.CommentResponse;
import com.cow.cow_instagram_practice.comment.entity.Comment;
import com.cow.cow_instagram_practice.comment.repository.CommentRepository;
import com.cow.cow_instagram_practice.image.entity.PostImage;
import com.cow.cow_instagram_practice.member.entity.Member;
import com.cow.cow_instagram_practice.post.entity.Post;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
	@InjectMocks
	CommentServiceImpl commentService;

	@Mock
	CommentRepository commentRepository;

	@Test
	@DisplayName("댓글 생성하기")
	public void createComment() {
		Member member = Member.builder().id("0711kc").build();
		PostImage postImage = PostImage.builder().imageLink("testLink").build();
		Post post = Post.builder().id(1L).postImage(postImage).build();
		CommentRequest commentRequest = CommentRequest.builder().content("Comment's Content").build();
		Comment comment = Comment.builder().member(member).post(post).build();
		given(commentRepository.save(any(Comment.class))).willReturn(comment);

		ResponseEntity<CommentResponse> responseEntity = commentService.create(member, post, commentRequest);
		CommentResponse commentResponse = responseEntity.getBody();

		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Assertions.assertThat(commentResponse).isNotNull();
		verify(commentRepository).save(any(Comment.class));
	}

	@Test
	@DisplayName("답글 생성하기")
	public void createReply() {
		Long parentCommentId = 1L;
		Long commentId = 2L;
		Member member = Member.builder().id("0711kc").build();
		PostImage postImage = PostImage.builder().imageLink("testLink").build();
		Post post = Post.builder().id(1L).postImage(postImage).build();
		CommentRequest commentRequest = CommentRequest.builder().content("Comment's Content").build();
		Comment paraentComment = Comment.builder().id(parentCommentId).member(member).post(post).build();
		Comment reply = Comment.builder().id(commentId).member(member).post(post).parent(paraentComment).build();
		given(commentRepository.findById(parentCommentId)).willReturn(Optional.ofNullable(paraentComment));
		given(commentRepository.save(any(Comment.class))).willReturn(reply);

		ResponseEntity<CommentResponse> responseEntity = commentService.createReply(member, post, parentCommentId,
			commentRequest);
		CommentResponse commentResponse = responseEntity.getBody();

		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Assertions.assertThat(commentResponse).isNotNull();
		verify(commentRepository).findById(parentCommentId);
		verify(commentRepository).save(any(Comment.class));
	}

	@Test
	@DisplayName("댓글 불러오기")
	public void getcomment() {
		Long commentId = 1L;
		Member member = Member.builder().id("0711kc").name("test member").build();
		PostImage postImage = PostImage.builder().imageLink("testLink").build();
		Post post = Post.builder().id(1L).postImage(postImage).build();
		Comment comment = Comment.builder().member(member).post(post).build();
		given(commentRepository.findById(commentId)).willReturn(Optional.ofNullable(comment));

		ResponseEntity<CommentResponse> responseEntity = commentService.findOne(commentId);
		CommentResponse commentResponse = responseEntity.getBody();

		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(commentResponse).isNotNull();
		verify(commentRepository).findById(commentId);
	}

	@Test
	@DisplayName("특정 회원의 댓글 불러오기")
	public void getCommentsByMember() {
		String memberId = "0711kc";
		given(commentRepository.findByMemberId(memberId)).willReturn(new ArrayList<>());

		ResponseEntity<List<CommentResponse>> responseEntity = commentService.findByMember(memberId);
		List<CommentResponse> commentResponses = responseEntity.getBody();

		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(commentResponses).isNotNull();
		verify(commentRepository).findByMemberId(memberId);
	}

	@Test
	@DisplayName("특정 게시글의 댓글 불러오기")
	public void getCommentsByPost() {
		Long postId = 1L;
		given(commentRepository.findByPostId(postId)).willReturn(new ArrayList<>());

		ResponseEntity<List<CommentResponse>> responseEntity = commentService.findByPost(postId);
		List<CommentResponse> commentResponses = responseEntity.getBody();

		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(commentResponses).isNotNull();
		verify(commentRepository).findByPostId(postId);
	}

	@Test
	@DisplayName("특정 댓글의 답글 불러오기")
	public void getReplies() {
		Long parentCommentId = 1L;
		given(commentRepository.findByParentId(parentCommentId)).willReturn(new ArrayList<>());

		ResponseEntity<List<CommentResponse>> responseEntity = commentService.findReplies(parentCommentId);
		List<CommentResponse> commentResponses = responseEntity.getBody();

		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(commentResponses).isNotNull();
		verify(commentRepository).findByParentId(parentCommentId);
	}

	@Test
	@DisplayName("댓글 수정하기")
	public void updateComment() {
		Long commentId = 1L;
		Member member = Member.builder().id("0711kc").name("test member").build();
		PostImage postImage = PostImage.builder().imageLink("testLink").build();
		Post post = Post.builder().id(1L).postImage(postImage).build();
		Comment comment = Comment.builder().member(member).post(post).build();
		UpdateCommentRequest updateCommentRequest = UpdateCommentRequest.builder().content("Comment's Content").build();
		given(commentRepository.findById(commentId)).willReturn(Optional.ofNullable(comment));

		ResponseEntity<CommentResponse> responseEntity = commentService.update(commentId, updateCommentRequest);
		CommentResponse commentResponse = responseEntity.getBody();

		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(commentResponse).isNotNull();
		verify(commentRepository).findById(commentId);
		verify(commentRepository).save(any(Comment.class));
	}

	@Test
	@DisplayName("댓글 삭제하기")
	public void deleteComment() {
		Long commentId = 1L;
		given(commentRepository.existsById(commentId)).willReturn(true);

		ResponseEntity<Void> responseEntity = commentService.delete(commentId);

		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		verify(commentRepository).existsById(commentId);
		verify(commentRepository).deleteById(commentId);
	}
}
