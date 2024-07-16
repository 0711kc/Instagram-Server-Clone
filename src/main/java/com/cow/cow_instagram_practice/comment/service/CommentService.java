package com.cow.cow_instagram_practice.comment.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cow.cow_instagram_practice.comment.controller.dto.request.CommentRequest;
import com.cow.cow_instagram_practice.comment.controller.dto.request.UpdateCommentRequest;
import com.cow.cow_instagram_practice.comment.controller.dto.response.CommentResponse;
import com.cow.cow_instagram_practice.member.entity.Member;
import com.cow.cow_instagram_practice.post.entity.Post;

public interface CommentService {
	ResponseEntity<CommentResponse> create(Member member, Post post, CommentRequest commentRequest);

	ResponseEntity<CommentResponse> createReply(Member member, Post post, Long commentId,
		CommentRequest commentRequest);

	ResponseEntity<CommentResponse> findOne(Long commentId);

	ResponseEntity<List<CommentResponse>> findByMember(String memberId);

	ResponseEntity<List<CommentResponse>> findByPost(Long postId);

	ResponseEntity<List<CommentResponse>> findReplies(Long commentId);

	ResponseEntity<Void> delete(Long commentId);

	ResponseEntity<CommentResponse> update(Long commentId, UpdateCommentRequest updateCommentRequest);
}
