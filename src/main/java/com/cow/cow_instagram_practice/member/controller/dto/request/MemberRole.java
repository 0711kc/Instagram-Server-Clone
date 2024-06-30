package com.cow.cow_instagram_practice.member.controller.dto.request;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MemberRole {
	User("user", (short)0),
	Admin("admin", (short)1);

	private final String description;

	@Getter
	private final Short index;

	@JsonCreator
	public static MemberRole from(String role) {
		return Arrays.stream(MemberRole.values())
			.filter(memberRole -> memberRole.getDescription().equals(role))
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}

	public static String getDescription(Short index) {
		return Arrays.stream(MemberRole.values())
			.filter(memberRole -> memberRole.getIndex().equals(index))
			.findFirst()
			.orElseThrow(IllegalArgumentException::new)
			.getDescription();
	}

	@JsonValue
	public String getDescription() {
		return description;
	}
}
