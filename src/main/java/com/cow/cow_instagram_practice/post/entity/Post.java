package com.cow.cow_instagram_practice.post.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cow.cow_instagram_practice.comment.entity.Comment;
import com.cow.cow_instagram_practice.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 100)
	private String content;

	@Builder.Default
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime date = LocalDateTime.now();

	@Size(max = 150)
	private String image;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "member_id")
	private Member member;

	@Builder.Default
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	private final List<Comment> comment = new ArrayList<>();

	public static Post of(final Long id, final String content, final LocalDateTime date, final String image,
		final Member member) {
		return Post.builder()
			.id(id)
			.content(content)
			.date(date)
			.image(image)
			.member(member)
			.build();
	}
}
