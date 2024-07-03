package com.cow.cow_instagram_practice.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cow.cow_instagram_practice.member.entity.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long> {
	List<Follow> findByFollowerId(String follower);
	List<Follow> findByFollowingId(String following);
}
