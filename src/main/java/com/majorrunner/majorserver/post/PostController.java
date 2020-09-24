package com.majorrunner.majorserver.post;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/posts", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class PostController {

    private final ModelMapper modelMapper;
    private final PostRepository postRepository;
    private final PostService postService;

    @PostMapping
    public ResponseEntity createPost(@RequestBody @Valid PostDto postDto) {

        Post post = modelMapper.map(postDto, Post.class);

        postService.add(post);

        ControllerLinkBuilder selfLinkBuilder = linkTo(PostController.class).slash(post.getId());
        URI createdUri = selfLinkBuilder.toUri();

        return ResponseEntity.created(createdUri).body(post);
    }

    @GetMapping("/{id}")
    public ResponseEntity getPost(@PathVariable Long id) {

        Post post = postService.findOne(id);

        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        PostDto postDto = modelMapper.map(post, PostDto.class);

        ControllerLinkBuilder selfLinkBuilder = linkTo(PostController.class).slash(post.getId());
        URI createdUri = selfLinkBuilder.toUri();

        return ResponseEntity.ok().body(postDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity updatePost(@PathVariable Long id, @RequestBody @Valid PostDto postDto) {
        
        Post post = postService.findOne(id);

        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        postService.update(postDto, post);

        ControllerLinkBuilder selfLinkBuilder = linkTo(PostController.class).slash(post.getId());
        URI createdUri = selfLinkBuilder.toUri();

        return ResponseEntity.created(createdUri).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePost(@PathVariable Long id) {

        Post post = postService.findOne(id);

        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        postService.delete(post);

        return ResponseEntity.noContent().build();
    }

}
