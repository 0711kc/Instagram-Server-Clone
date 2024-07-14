package com.cow.cow_instagram_practice.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cow.cow_instagram_practice.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String>  {
	@Query("select m from Member m left join fetch m.posts p left join fetch p.postImage where m.id =:id")
	Optional<Member> findByIdJoinFetch(String id);
}
