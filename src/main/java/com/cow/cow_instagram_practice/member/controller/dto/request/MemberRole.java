package com.cow.cow_instagram_practice.member.controller.dto.request;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {
	User("user", "ROLE_USER"),
	Admin("admin", "ROLE_ADMIN");

	private final String description;

	private final String permission;

	@JsonValue
	public String getDescription() {
		return description;
	}

	@JsonCreator
	public static MemberRole from(String description) {
		return Arrays.stream(MemberRole.values())
			.filter(memberRole -> memberRole.getDescription().equals(description))
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}

	public static MemberRole findByPermission(String permission) {
		return Arrays.stream(MemberRole.values())
			.filter(memberRole -> memberRole.getPermission().equals(permission))
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}
}
