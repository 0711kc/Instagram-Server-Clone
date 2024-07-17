package com.cow.cow_instagram_practice.post.service;

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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cow.cow_instagram_practice.follow.repository.FollowRepository;
import com.cow.cow_instagram_practice.image.entity.PostImage;
import com.cow.cow_instagram_practice.member.entity.Member;
import com.cow.cow_instagram_practice.post.controller.dto.request.PostRequest;
import com.cow.cow_instagram_practice.post.controller.dto.request.UpdatePostRequest;
import com.cow.cow_instagram_practice.post.controller.dto.response.PostResponse;
import com.cow.cow_instagram_practice.post.entity.Post;
import com.cow.cow_instagram_practice.post.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
	@InjectMocks
	PostServiceImpl postService;

	@Mock
	PostRepository postRepository;

	@Mock
	FollowRepository followRepository;

	@Test
	@DisplayName("게시글 등록하기")
	public void createPost() {
		PostRequest postRequest = PostRequest.builder().content("Post's Content").build();
		Member member = Member.builder().id("0711kc").build();
		PostImage postImage = PostImage.builder().build();
		Post post = Post.builder().member(member).postImage(postImage).build();
		given(postRepository.save(any(Post.class))).willReturn(post);

		ResponseEntity<PostResponse> postResponseEntity = postService.create(member, postRequest, postImage);
		PostResponse postResponse = postResponseEntity.getBody();

		Assertions.assertThat(postResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Assertions.assertThat(postResponse).isNotNull();
		verify(postRepository).save(any(Post.class));
	}

	@Test
	@DisplayName("게시글 불러오기")
	public void getPost() {
		Long postId = 1L;
		Post post = Post.builder().build();
		given(postRepository.findByIdJoinFetch(postId)).willReturn(Optional.ofNullable(post));

		Post findPost = postService.findOne(postId);

		Assertions.assertThat(findPost).isNotNull();
		verify(postRepository).findByIdJoinFetch(postId);
	}

	@Test
	@DisplayName("모든 게시글 불러오기")
	public void getAllPosts() {
		Long cursor = 0L;
		given(postRepository.findAll(any(PageRequest.class))).willReturn(new PageImpl<>(new ArrayList<>()));
		given(postRepository.findNextPage(anyLong(), any(PageRequest.class))).willReturn(
			new PageImpl<>(new ArrayList<>()));

		ResponseEntity<List<PostResponse>> postsResponseEntity = postService.findAll(cursor);
		cursor = 2L;
		ResponseEntity<List<PostResponse>> postsResponseEntityUseCursor = postService.findAll(cursor);
		List<PostResponse> postResponses = postsResponseEntity.getBody();
		List<PostResponse> postResponsesUserCursor = postsResponseEntity.getBody();

		Assertions.assertThat(postsResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(postResponses).isNotNull();
		Assertions.assertThat(postsResponseEntityUseCursor.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(postResponsesUserCursor).isNotNull();
		verify(postRepository).findAll(any(PageRequest.class));
		verify(postRepository).findNextPage(anyLong(), any(PageRequest.class));
	}

	@Test
	@DisplayName("특정 회원의 게시글 불러오기")
	public void getAllPostsByMember() {
		String memberId = "0711kc";
		Member member = Member.builder().id(memberId).build();
		given(postRepository.findByMemberIdJoinFetch(memberId)).willReturn(new ArrayList<>());

		ResponseEntity<List<PostResponse>> responseEntity = postService.findAllByMember(member);
		List<PostResponse> postResponses = responseEntity.getBody();

		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(postResponses).isNotNull();
		verify(postRepository).findByMemberIdJoinFetch(memberId);
	}

	@Test
	@DisplayName("피드 불러오기")
	public void getFeed() {
		Long cursor = 0L;
		String followerId = "0711kc";
		given(followRepository.findByFollowerId(followerId)).willReturn(new ArrayList<>());
		given(postRepository.findByMemberIdIn(any(PageRequest.class), any(List.class))).willReturn(
			new PageImpl<>(new ArrayList<>()));
		given(postRepository.findByMemberIdIn(anyLong(), any(PageRequest.class), any(List.class))).willReturn(
			new PageImpl<>(new ArrayList<>()));

		ResponseEntity<List<PostResponse>> responseEntity = postService.getFeed(followerId, cursor);
		List<PostResponse> postResponses = responseEntity.getBody();
		cursor = 2L;
		ResponseEntity<List<PostResponse>> responseEntityUseCursor = postService.getFeed(followerId, cursor);
		List<PostResponse> postResponsesUseCursor = responseEntityUseCursor.getBody();

		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(postResponses).isNotNull();
		Assertions.assertThat(responseEntityUseCursor.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(postResponsesUseCursor).isNotNull();
		verify(followRepository, times(2)).findByFollowerId(followerId);
		verify(postRepository).findByMemberIdIn(any(PageRequest.class), any(List.class));
		verify(postRepository).findByMemberIdIn(anyLong(), any(PageRequest.class), any(List.class));
	}

	@Test
	@DisplayName("게시글 수정하기")
	public void updatePost() {
		Long postId = 1L;
		UpdatePostRequest updatePostRequest = UpdatePostRequest.builder().content("Post's Content").build();
		Member member = Member.builder().id("0711kc").build();
		PostImage postImage = PostImage.builder().build();
		Post post = Post.builder().id(postId).member(member).postImage(postImage).build();
		given(postRepository.findByIdJoinFetch(postId)).willReturn(Optional.ofNullable(post));

		ResponseEntity<PostResponse> postResponseEntity = postService.update(postId, updatePostRequest);
		PostResponse postResponse = postResponseEntity.getBody();

		Assertions.assertThat(postResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(postResponse).isNotNull();
		verify(postRepository).findByIdJoinFetch(postId);
		verify(postRepository).save(any(Post.class));
	}

	@Test
	@DisplayName("게시글 이미지 수정하기")
	public void updateImagePost() {
		Long postId = 1L;
		Member member = Member.builder().id("0711kc").build();
		PostImage postImage = PostImage.builder().build();
		PostImage updatePostImage = PostImage.builder().build();
		Post post = Post.builder().id(postId).member(member).postImage(postImage).build();
		given(postRepository.findByIdJoinFetch(postId)).willReturn(Optional.ofNullable(post));

		ResponseEntity<PostResponse> postResponseEntity = postService.update(postId, updatePostImage);
		PostResponse postResponse = postResponseEntity.getBody();

		Assertions.assertThat(postResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(postResponse).isNotNull();
		verify(postRepository).findByIdJoinFetch(postId);
		verify(postRepository).save(any(Post.class));
	}

	@Test
	@DisplayName("게시글 삭제하기")
	public void deletePost() {
		Long postId = 1L;
		given(postRepository.existsById(postId)).willReturn(true);

		ResponseEntity<Void> responseEntity = postService.delete(postId);

		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		verify(postRepository).existsById(postId);
		verify(postRepository).deleteById(postId);
	}
}
