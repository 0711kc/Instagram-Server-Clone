package com.cow.cow_instagram_practice.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cow.cow_instagram_practice.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	@Query("select p from Post p left join fetch p.member m left join fetch p.postImage where m.id = :memberId")
	List<Post> findByMemberIdJoinFetch(String memberId);

	@Query("select p from Post p left join fetch p.postImage where p.id = :id")
	Optional<Post> findByIdJoinFetch(Long id);
}
