package com.cow.cow_instagram_practice.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cow.cow_instagram_practice.image.entity.PostImage;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
