package com.cow.cow_instagram_practice.image.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.cow.cow_instagram_practice.member.entity.ProfileImage;

public interface ImageService {
	ProfileImage upload(MultipartFile image) throws IOException;
}
