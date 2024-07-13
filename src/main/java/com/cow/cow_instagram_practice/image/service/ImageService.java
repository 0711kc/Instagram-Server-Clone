package com.cow.cow_instagram_practice.image.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.cow.cow_instagram_practice.image.entity.PostImage;
import com.cow.cow_instagram_practice.image.entity.ProfileImage;

public interface ImageService {
	ProfileImage uploadProfileImage(MultipartFile image) throws IOException;
	PostImage uploadPostImage(MultipartFile image) throws IOException;
}
