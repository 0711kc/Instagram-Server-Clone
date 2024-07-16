package com.cow.cow_instagram_practice.comment.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cow.cow_instagram_practice.comment.controller.dto.request.CommentRequest;
import com.cow.cow_instagram_practice.comment.controller.dto.request.UpdateCommentRequest;
import com.cow.cow_instagram_practice.comment.controller.dto.response.CommentResponse;
import com.cow.cow_instagram_practice.comment.entity.Comment;
import com.cow.cow_instagram_practice.comment.repository.CommentRepository;
import com.cow.cow_instagram_practice.member.entity.Member;
import com.cow.cow_instagram_practice.post.entity.Post;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {
	private final CommentRepository commentRepository;

	@Override
	public ResponseEntity<CommentResponse> create(Member member, Post post, CommentRequest commentRequest) {
		Comment comment = commentRequest.toEntity(LocalDateTime.now(), member, post, null);
		Comment savedComment = commentRepository.save(comment);
		return ResponseEntity.status(HttpStatus.CREATED)
			.contentType(MediaType.APPLICATION_JSON)
			.body(CommentResponse.from(savedComment));
	}

	@Override
	public ResponseEntity<CommentResponse> createReply(Member member, Post post, Long commentId,
		CommentRequest commentRequest) {
		Comment parentComment = commentRepository.findById(commentId)
			.orElseThrow(() -> new EntityNotFoundException("[Error] 댓글을 찾을 수 없습니다."));
		if (parentComment.isReply()) {
			throw new IllegalArgumentException("[Error] 답글에는 답글을 달 수 없습니다.");
		}
		Comment comment = commentRequest.toEntity(LocalDateTime.now(), member, post, parentComment);
		Comment savedComment = commentRepository.save(comment);
		return ResponseEntity.status(HttpStatus.CREATED)
			.contentType(MediaType.APPLICATION_JSON)
			.body(CommentResponse.from(savedComment));
	}

	@Override
	public ResponseEntity<CommentResponse> findOne(Long commentId) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new EntityNotFoundException("[Error] 댓글을 찾을 수 없습니다."));
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(CommentResponse.from(comment));
	}

	@Override
	public ResponseEntity<List<CommentResponse>> findByMember(String memberId) {
		List<Comment> comments = commentRepository.findByMemberId(memberId);
		List<CommentResponse> responses = comments.stream()
			.map(CommentResponse::from)
			.toList();
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(responses);
	}

	@Override
	public ResponseEntity<List<CommentResponse>> findByPost(Long postId) {
		List<Comment> comments = commentRepository.findByPostId(postId);
		List<CommentResponse> responses = comments.stream()
			.map(CommentResponse::from)
			.toList();
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(responses);
	}

	@Override
	public ResponseEntity<List<CommentResponse>> findReplies(Long commentId) {
		List<Comment> comments = commentRepository.findByParentId(commentId);
		List<CommentResponse> responses = comments.stream()
			.map(CommentResponse::from)
			.toList();
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(responses);
	}

	@Override
	public ResponseEntity<CommentResponse> update(Long commentId, UpdateCommentRequest updateCommentRequest) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new EntityNotFoundException("[Error] 댓글을 찾을 수 없습니다."));
		comment.update(updateCommentRequest.getContent());
		commentRepository.save(comment);
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(CommentResponse.from(comment));
	}

	@Override
	public ResponseEntity<Void> delete(Long commentId) {
		boolean isExistComment = commentRepository.existsById(commentId);
		if (!isExistComment) {
			throw new EntityNotFoundException("[Error] 댓글을 찾을 수 없습니다.");
		}
		commentRepository.deleteById(commentId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
