package com.majorrunner.majorserver.post;

import com.majorrunner.majorserver.Like.Like;
import com.majorrunner.majorserver.comment.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /** post add */
    @Transactional
    public Long add(Post post) {
        Post save = postRepository.save(post);
        return post.getId();
    }

    /** post 수정 */
    @Transactional
    public void update(PostDto postDto, Post post) {

        post.updatePost(postDto, post);
        postRepository.save(post);
    }

    /** post 삭제 */
    @Transactional
    public void delete(Post post) {
        postRepository.delete(post);
    }

    /** post 조회 */
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    /** post paging */
    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    /** post 한건 조회 */
    public Post findOne(Long postId) {

        Optional<Post> optionalPost = postRepository.findById(postId);

        if (optionalPost.isPresent()) {
            return postRepository.getOne(postId);
        }else {
            return null;
        }

    }

    /** 좋아요 수 */
    public int likesNum(Long postId) {
        Post post = postRepository.getOne(postId);
        List<Like> likes = post.getLikes();
        return likes.size();
    }

    /** comment 추가 */
    public void savaComment(Post post, Comment comment) {
        post.addComment(comment);
    }

    /** comment 삭제 */
    public void deleteComment(Post post, Comment comment) {
        List<Comment> comments = post.getComments();
        comments.remove(comment);
    }

    /** comment status 변경 */
    @Transactional
    public void changeCommentType(Post post) {
        post.changeStatus();
        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);
    }
}
