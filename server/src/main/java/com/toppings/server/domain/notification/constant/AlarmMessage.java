package com.toppings.server.domain.notification.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmMessage {

	LikeMessage("liked your post."),
	ReviewMessage("reviewed your post"),
	ScrapMessage("scraped your post."),
	RejectMessage("Your post has bean rejected");

	private final String message;
}
