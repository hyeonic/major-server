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
    public ResponseEntity createPost(@RequestBody @Valid PostDto postDto) {

        Post post = modelMapper.map(postDto, Post.class);
        String username = post.getAccount().getUsername();
        Optional<Account> optionalAccount = accountRepository.findByUsername(username);

        if (!optionalAccount.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Account account = optionalAccount.get();
        post.setAccount(account);

        postService.add(post);

        ControllerLinkBuilder selfLinkBuilder = linkTo(PostController.class).slash(post.getId());
        URI createdUri = selfLinkBuilder.toUri();

        post.setAccount(postDto.getAccount());

        return ResponseEntity.created(createdUri).body(post);
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

        PostDto postDto = modelMapper.map(post, PostDto.class);

        ControllerLinkBuilder selfLinkBuilder = linkTo(PostController.class).slash(post.getId());
        URI createdUri = selfLinkBuilder.toUri();

        return ResponseEntity.ok().body(postDto);
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

    @GetMapping("/{id}/views")
    public void views(@PathVariable Long id) {
        Post post = postService.findOne(id);
        post.incrementViews();

        postRepository.save(post);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity createComment(@PathVariable Long id, @RequestBody CommentDto.CreateCommentRequest createCommentRequest) {

        Post post = postService.findOne(id);

        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        Comment comment = Comment.createComment(createCommentRequest.getComment(), post);
        Comment savedComment = commentRepository.save(comment);

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

        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity createLike(@PathVariable Long id, AccountDto.CreateAccountResponse accountInfo) {

        Post post = postService.findOne(id);

        Account account = modelMapper.map(accountInfo, Account.class);

        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        Like like = Like.createLike(post, account);
        Like savedLike = likeRepository.save(like);

        post.addLikes(savedLike);
        Post updatedPost = postRepository.save(post);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/like/{likeId}")
    public ResponseEntity deleteLike(@PathVariable Long postId, @PathVariable Long likeId) {

        Optional<Like> optionalLike = likeRepository.findById(likeId);

        if (!optionalLike.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Like like = optionalLike.get();
        likeRepository.delete(like);

        return ResponseEntity.noContent().build();
    }

}
