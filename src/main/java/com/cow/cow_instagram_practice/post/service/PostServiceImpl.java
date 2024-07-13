package com.cow.cow_instagram_practice.post.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cow.cow_instagram_practice.image.entity.PostImage;
import com.cow.cow_instagram_practice.member.entity.Member;
import com.cow.cow_instagram_practice.post.controller.dto.request.PostRequest;
import com.cow.cow_instagram_practice.post.controller.dto.response.PostResponse;
import com.cow.cow_instagram_practice.post.entity.Post;
import com.cow.cow_instagram_practice.post.repository.PostRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {
	private final PostRepository postRepository;

	@Override
	public ResponseEntity<PostResponse> create(Member member, PostRequest postRequest, PostImage postImage) {
		Post post = postRequest.toEntity(LocalDateTime.now(), postImage, member);
		Post savedPost = postRepository.save(post);
		return ResponseEntity.status(HttpStatus.CREATED)
			.contentType(MediaType.APPLICATION_JSON)
			.body(PostResponse.from(savedPost));
	}

	@Override
	public ResponseEntity<PostResponse> findOne(Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new EntityNotFoundException("[Error] 게시글을 찾을 수 없습니다."));
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(PostResponse.from(post));
	}

	@Override
	public ResponseEntity<List<PostResponse>> findAll() {
		List<PostResponse> responses = postRepository.findAll().stream()
			.map(PostResponse::from)
			.toList();
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(responses);
	}

	@Override
	public ResponseEntity<List<PostResponse>> findAllByMember(Member member) {
		List<PostResponse> responses = postRepository.findByMemberId(member.getId()).stream()
			.map(PostResponse::from)
			.toList();
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(responses);
	}
}
