package com.cow.cow_instagram_practice.post.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cow.cow_instagram_practice.image.entity.PostImage;
import com.cow.cow_instagram_practice.image.service.ImageService;
import com.cow.cow_instagram_practice.member.entity.Member;
import com.cow.cow_instagram_practice.member.service.MemberService;
import com.cow.cow_instagram_practice.post.controller.dto.request.PostRequest;
import com.cow.cow_instagram_practice.post.controller.dto.response.PostResponse;
import com.cow.cow_instagram_practice.post.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
	private final PostService postService;
	private final ImageService imageService;
	private final MemberService memberService;

	@PostMapping("/new/{memberId}")
	public ResponseEntity<PostResponse> create(@RequestPart(value = "request") @Valid final PostRequest postRequest,
		@PathVariable String memberId, @RequestPart(value = "image") MultipartFile multipartFile) throws IOException {
		PostImage postImage = imageService.uploadPostImage(multipartFile);
		Member member = memberService.findOne(memberId);
		return postService.create(member, postRequest, postImage);
	}

	@GetMapping("/{postId}")
	public ResponseEntity<PostResponse> findPost(@PathVariable Long postId) {
		return postService.findOne(postId);
	}

	@GetMapping("/{memberId}")
	public ResponseEntity<List<PostResponse>> findAllByMember(@PathVariable String memberId) {
		Member member = memberService.findOne(memberId);
		return postService.findAllByMember(member);
	}

	@GetMapping("/all")
	public ResponseEntity<List<PostResponse>> findAll() {
		return postService.findAll();
	}
}
