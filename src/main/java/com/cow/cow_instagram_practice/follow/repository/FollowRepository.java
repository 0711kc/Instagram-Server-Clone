package com.cow.cow_instagram_practice.follow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cow.cow_instagram_practice.follow.entity.Follow;
import com.cow.cow_instagram_practice.follow.entity.FollowId;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {
	List<Follow> findByFollowerId(String follower);

	List<Follow> findByFollowingId(String following);
}
