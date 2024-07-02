package com.cow.cow_instagram_practice.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cow.cow_instagram_practice.member.entity.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
