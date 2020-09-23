package com.majorrunner.majorserver.post;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class PostResource extends Resource<Post> {

    public PostResource(Post post, Link...liks) {
        super(post, liks);
        add(linkTo(PostController.class).slash(post.getId()).withSelfRel());
    }
}
