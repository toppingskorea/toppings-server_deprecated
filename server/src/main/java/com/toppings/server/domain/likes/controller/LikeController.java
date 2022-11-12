package com.toppings.server.domain.likes.controller;

import com.toppings.server.domain.likes.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
}
