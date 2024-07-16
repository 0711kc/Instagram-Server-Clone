package com.cow.cow_instagram_practice.comment.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.cow.cow_instagram_practice.member.entity.Member;
import com.cow.cow_instagram_practice.post.entity.Post;
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
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 50)
	private String content;

	@Builder.Default
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime date = LocalDateTime.now();

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "post_id")
	private Post post;

	@OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
	private List<Comment> child;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "parent_id")
	private Comment parent;

	public static Comment of(final String content, final LocalDateTime date, final Member member, final Post post,
		final Comment parent) {
		return Comment.builder()
			.content(content)
			.date(date)
			.member(member)
			.post(post)
			.parent(parent)
			.build();
	}

	public boolean isReply() {
		return parent != null;
	}

	public void update(String content) {
		this.content = content;
	}
}
