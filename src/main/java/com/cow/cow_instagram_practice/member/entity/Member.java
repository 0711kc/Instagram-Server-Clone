package com.cow.cow_instagram_practice.member.entity;

import java.util.ArrayList;
import java.util.List;

import com.cow.cow_instagram_practice.comment.entity.Comment;
import com.cow.cow_instagram_practice.image.entity.ProfileImage;
import com.cow.cow_instagram_practice.post.entity.Post;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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
public class Member {
	@Id
	@Size(max = 12)
	private String id;

	@Size(max = 80)
	private String password;

	@Size(max = 8)
	private String name;

	@Size(max = 20)
	private String nickname;

	@Size(max = 14)
	private String phone;

	@Size(max = 50)
	private String email;

	@ManyToOne(fetch = FetchType.LAZY)
	private ProfileImage profileImage;

	@Size(max = 12)
	private String role;

	@Builder.Default
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private final List<Post> posts = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private final List<Comment> comments = new ArrayList<>();

	public void updatePassword(String password) {
		this.password = password;
	}

	public void update(String name, String nickname, String phone, String email, String role) {
		this.name = name;
		this.nickname = nickname;
		this.phone = phone;
		this.email = email;
		this.role = role;
	}

	public void updateProfileImage(ProfileImage profileImage) {
		this.profileImage = profileImage;
	}
}
