package com.cow.cow_instagram_practice.post.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cow.cow_instagram_practice.image.entity.PostImage;
import com.cow.cow_instagram_practice.member.entity.Member;
import com.cow.cow_instagram_practice.post.controller.dto.request.PostRequest;
import com.cow.cow_instagram_practice.post.controller.dto.request.UpdatePostRequest;
import com.cow.cow_instagram_practice.post.controller.dto.response.PostResponse;
import com.cow.cow_instagram_practice.post.entity.Post;

public interface PostService {
	ResponseEntity<PostResponse> create(Member member, PostRequest postRequest, PostImage postImage);

	Post findOne(Long postId);

	ResponseEntity<List<PostResponse>> findAll(Long cursor);

	ResponseEntity<List<PostResponse>> findAllByMember(Member member);

	ResponseEntity<PostResponse> update(Long postId, UpdatePostRequest updatePostRequest);

	ResponseEntity<PostResponse> update(Long postId, PostImage postImage);

	ResponseEntity<Void> delete(Long postId);
}
