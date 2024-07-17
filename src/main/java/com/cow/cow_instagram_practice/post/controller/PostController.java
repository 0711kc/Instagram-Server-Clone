package com.cow.cow_instagram_practice.post.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cow.cow_instagram_practice.image.entity.PostImage;
import com.cow.cow_instagram_practice.image.service.ImageService;
import com.cow.cow_instagram_practice.member.entity.Member;
import com.cow.cow_instagram_practice.member.service.MemberService;
import com.cow.cow_instagram_practice.post.controller.dto.request.PostRequest;
import com.cow.cow_instagram_practice.post.controller.dto.request.UpdatePostRequest;
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

	@PostMapping("/new")
	public ResponseEntity<PostResponse> create(@RequestPart(value = "request") @Valid final PostRequest postRequest,
		@RequestPart(value = "image") MultipartFile multipartFile, Principal principal) throws IOException {
		PostImage postImage = imageService.uploadPostImage(multipartFile);
		Member member = memberService.findOne(principal.getName());
		return postService.create(member, postRequest, postImage);
	}

	@GetMapping("/{postId}")
	public ResponseEntity<PostResponse> findPost(@PathVariable Long postId) {
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(PostResponse.from(postService.findOne(postId)));
	}

	@GetMapping("/all/my")
	public ResponseEntity<List<PostResponse>> findMyPostAll(Principal principal) {
		Member member = memberService.findOne(principal.getName());
		return postService.findAllByMember(member);
	}

	@GetMapping("/all")
	public ResponseEntity<List<PostResponse>> findAll(@RequestParam(value = "cursor") Long cursor) {
		return postService.findAll(cursor);
	}

	@PatchMapping("/{postId}")
	public ResponseEntity<PostResponse> update(@PathVariable Long postId,
		@RequestBody @Valid UpdatePostRequest updatePostRequest) {
		return postService.update(postId, updatePostRequest);
	}

	@PatchMapping("/{postId}/image")
	public ResponseEntity<PostResponse> updatePostImage(@PathVariable Long postId,
		@RequestParam("image") MultipartFile multipartFile) throws IOException {
		PostImage postImage = imageService.uploadPostImage(multipartFile);
		return postService.update(postId, postImage);
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<Void> delete(@PathVariable Long postId) {
		return postService.delete(postId);
	}

	@GetMapping("/feed")
	public ResponseEntity<List<PostResponse>> getFeed(@RequestParam(value = "cursor") Long cursor,
		Principal principal) {
		return postService.getFeed(principal.getName(), cursor);
	}
}
