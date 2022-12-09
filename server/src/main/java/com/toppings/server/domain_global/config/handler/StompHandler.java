package com.toppings.server.domain_global.config.handler;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

	@Override
	public Message<?> preSend(
		Message<?> message,
		MessageChannel channel
	) {
		StompHeaderAccessor accessor =
			MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		log.debug("accessor.getCommand() : " + accessor.getCommand());
		return message;
	}

}
