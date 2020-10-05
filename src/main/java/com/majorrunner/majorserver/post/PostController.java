package com.majorrunner.majorserver.post;

import com.majorrunner.majorserver.Like.Like;
import com.majorrunner.majorserver.Like.LikeRepository;
import com.majorrunner.majorserver.account.Account;
import com.majorrunner.majorserver.account.AccountDto;
import com.majorrunner.majorserver.account.AccountRepository;
import com.majorrunner.majorserver.category.Category;
import com.majorrunner.majorserver.category.CategoryRepository;
import com.majorrunner.majorserver.comment.Comment;
import com.majorrunner.majorserver.comment.CommentDto;
import com.majorrunner.majorserver.comment.CommentRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/posts", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class PostController {

    private final ModelMapper modelMapper;
    private final PostRepository postRepository;
    private final PostService postService;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final AccountRepository accountRepository;

    @PostMapping
    public ResponseEntity createPost(@RequestBody @Valid PostDto.CreatePostRequest postDto) {

        Post post = modelMapper.map(postDto, Post.class);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        String username = postDto.getAccount().getUsername();
        Optional<Account> optionalAccount = accountRepository.findByUsername(username);

        if (!optionalAccount.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Account account = optionalAccount.get();
        post.setAccount(account);

        postService.add(post);

        ControllerLinkBuilder selfLinkBuilder = linkTo(PostController.class).slash(post.getId());
        URI createdUri = selfLinkBuilder.toUri();

        PostDto.ReadPostResponse postResponse = new PostDto.ReadPostResponse(post);

        return ResponseEntity.created(createdUri).body(postResponse);
    }

    @GetMapping
    public ResponseEntity queryPosts(Pageable pageable,
                                     PagedResourcesAssembler<Post> assembler) {

        Page<Post> page = postRepository.findAll(pageable);
        PagedResources<PostResource> pagedResources = assembler.toResource(page, e -> new PostResource(e));

        return ResponseEntity.ok().body(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getPost(@PathVariable Long id) {

        Post post = postService.findOne(id);

        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        ControllerLinkBuilder selfLinkBuilder = linkTo(PostController.class).slash(post.getId());
        URI createdUri = selfLinkBuilder.toUri();

        PostDto.ReadPostResponse postResponse = new PostDto.ReadPostResponse(post);

        return ResponseEntity.ok().body(postResponse);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity postsByCategory(@PathVariable Long id,
                                          Pageable pageable) {

        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (!optionalCategory.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Category category = optionalCategory.get();
        Page<Post> page = postRepository.findByCategory(category, pageable);

        return ResponseEntity.ok().body(page);
    }

    @PutMapping("/{id}")
    public ResponseEntity updatePost(@PathVariable Long id, @RequestBody @Valid PostDto.CreatePostRequest postDto) {

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

    @GetMapping("/{id}/views")
    public void views(@PathVariable Long id) {
        Post post = postService.findOne(id);
        post.incrementViews();
        postRepository.save(post);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity createComment(@PathVariable Long id, @RequestBody CommentDto.CreateCommentRequest createCommentRequest) {

        Post post = postService.findOne(id);
        Optional<Account> optionalAccount = accountRepository.findByUsername(createCommentRequest.getAccount().getUsername());

        if (post == null || !optionalAccount.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Account account = optionalAccount.get();

        Comment comment = Comment.createComment(createCommentRequest.getComment(), post);
        Comment savedComment = commentRepository.save(comment);

        account.addComment(comment);
        post.addComment(savedComment);

        Post updatedPost = postRepository.save(post);// comment를 더하고 다시 save

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/comment")
    public ResponseEntity queryComment(@PathVariable Long id) {

        Post post = postService.findOne(id);

        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        List<Comment> comments = post.getComments();
        List<CommentDto.QueryCommentResponse> responseComments = new ArrayList<>();

        for (Comment comment : comments) {
            CommentDto.QueryCommentResponse queryCommentResponse = new CommentDto.QueryCommentResponse(comment);
            responseComments.add(queryCommentResponse);
        }

        return ResponseEntity.ok(responseComments);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity createLike(@PathVariable Long id,
                                     @RequestBody AccountDto.CreateAccountResponse accountInfo) {

        Post post = postService.findOne(id);
        Optional<Account> optionalAccount = accountRepository.findByUsername(accountInfo.getUsername());

        if (post == null || !optionalAccount.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Account account = optionalAccount.get();

        Like like = Like.createLike(post, account);
        Like savedLike = likeRepository.save(like);

        post.addLikes(savedLike);
        Post updatedPost = postRepository.save(post);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/like/{nickName}")
    public ResponseEntity deleteLike(@PathVariable(name = "postId") Long postId,
                                     @PathVariable(name = "nickName") String nickName) {

        Optional<Post> optionalPost = postRepository.findById(postId);

        if (!optionalPost.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Post post = optionalPost.get();

        List<Like> likes = likeRepository.findByPost(post);

        if (likes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Like deletedLike;

        for (Like like : likes) {
            if (like.getAccount().getNickName().equals(nickName)) {
                deletedLike = like;
                likeRepository.delete(deletedLike);
            }
        }

        return ResponseEntity.noContent().build();
    }

}
