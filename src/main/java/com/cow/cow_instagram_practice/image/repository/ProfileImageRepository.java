package com.cow.cow_instagram_practice.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cow.cow_instagram_practice.image.entity.ProfileImage;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
	Long DEFAULT_PROFILE_ID = 1L;
}
