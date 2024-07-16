package com.cow.cow_instagram_practice.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cow.cow_instagram_practice.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByMemberId(String memberId);

	List<Comment> findByPostId(Long postId);

	List<Comment> findByParentId(Long commentId);
}
