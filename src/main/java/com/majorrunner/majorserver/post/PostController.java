package com.majorrunner.majorserver.post;

import com.majorrunner.majorserver.Like.Like;
import com.majorrunner.majorserver.category.Category;
import com.majorrunner.majorserver.comment.Comment;
import com.majorrunner.majorserver.user.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/posts", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;

    @PostMapping
    public ResponseEntity createPost(@RequestBody @Valid Post post) {
        Long postId = postService.add(post);

        ControllerLinkBuilder selfLinkBuilder = linkTo(PostController.class).slash(post.getId());
        URI createdUri = selfLinkBuilder.toUri();

        return ResponseEntity.created(createdUri).body(post);
    }

    @GetMapping
    public ResponseEntity queryPosts(Pageable pageable, PagedResourcesAssembler<Post> assbler) {

        Page<Post> page = postRepository.findAll(pageable);
        PagedResources<PostResource> pagedResources = assbler.toResource(page, e -> new PostResource(e));

        return ResponseEntity.ok().body(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getPost(@PathVariable Long id) {

        Post post = postService.findOne(id);
        PostDto postDto = new PostDto(post);
        ControllerLinkBuilder selfLinkBuilder = linkTo(PostController.class).slash(post.getId());
        URI createdUri = selfLinkBuilder.toUri();

        return ResponseEntity.ok().body(postDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity updatePost(@PathVariable Long id, Post post) {

        Post updatedPost = postService.update(post);

        ControllerLinkBuilder selfLinkBuilder = linkTo(PostController.class).slash(post.getId());
        URI createdUri = selfLinkBuilder.toUri();

        return ResponseEntity.created(createdUri).body(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePost(@PathVariable Long id) {

        Post post = postService.findOne(id);
        postService.delete(post);

        return ResponseEntity.noContent().build();
    }

    @Data
    static class PostDto {

        private Long postId;
        private Article article;
        private int views;
        private User user;
        private Category category;
        private List<Comment> comments;
        private List<Like> likes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public PostDto(Post post) {
            postId = post.getId();
            article = post.getArticle();
            views = post.getViews();
            user = post.getUser();
            category = post.getCategory();
            comments = post.getComments();
            likes = post.getLikes();
            createdAt = post.getCreatedAt();
            updatedAt = post.getUpdatedAt();
        }
    }
}
