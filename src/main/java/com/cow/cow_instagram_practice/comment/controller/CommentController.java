package com.cow.cow_instagram_practice.comment.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cow.cow_instagram_practice.comment.controller.dto.request.CommentRequest;
import com.cow.cow_instagram_practice.comment.controller.dto.request.UpdateCommentRequest;
import com.cow.cow_instagram_practice.comment.controller.dto.response.CommentResponse;
import com.cow.cow_instagram_practice.comment.service.CommentService;
import com.cow.cow_instagram_practice.member.entity.Member;
import com.cow.cow_instagram_practice.member.service.MemberService;
import com.cow.cow_instagram_practice.post.entity.Post;
import com.cow.cow_instagram_practice.post.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
	private final CommentService commentService;
	private final MemberService memberService;
	private final PostService postService;

	@PostMapping("/new/{postId}")
	public ResponseEntity<CommentResponse> createComment(@PathVariable final Long postId,
		@RequestBody @Valid final CommentRequest commentRequest, Principal principal) {
		Member member = memberService.findOne(principal.getName());
		Post post = postService.findOne(postId);
		return commentService.create(member, post, commentRequest);
	}

	@PostMapping("/new/{postId}/{commentId}")
	public ResponseEntity<CommentResponse> createReply(@PathVariable final Long postId,
		@PathVariable final Long commentId,
		@RequestBody @Valid final CommentRequest commentRequest, Principal principal) {
		Member member = memberService.findOne(principal.getName());
		Post post = postService.findOne(postId);
		return commentService.createReply(member, post, commentId, commentRequest);
	}

	@GetMapping("/{commentId}")
	public ResponseEntity<CommentResponse> find(@PathVariable final Long commentId) {
		return commentService.findOne(commentId);
	}

	@GetMapping()
	public ResponseEntity<List<CommentResponse>> findByMember(Principal principal) {
		return commentService.findByMember(principal.getName());
	}

	@GetMapping("/post/{postId}")
	public ResponseEntity<List<CommentResponse>> findByPost(@PathVariable final Long postId) {
		return commentService.findByPost(postId);
	}

	@GetMapping("/reply/{commentId}")
	public ResponseEntity<List<CommentResponse>> findReplies(@PathVariable final Long commentId) {
		return commentService.findReplies(commentId);
	}

	@PatchMapping("/{commentId}")
	public ResponseEntity<CommentResponse> update(@PathVariable final Long commentId,
		@RequestBody @Valid UpdateCommentRequest updateCommentRequest) {
		return commentService.update(commentId, updateCommentRequest);
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<Void> delete(@PathVariable final Long commentId) {
		return commentService.delete(commentId);
	}
}
