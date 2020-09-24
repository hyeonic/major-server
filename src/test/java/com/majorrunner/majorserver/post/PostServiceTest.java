package com.majorrunner.majorserver.post;

import com.majorrunner.majorserver.category.Category;
import com.majorrunner.majorserver.comment.CommentStatus;
import com.majorrunner.majorserver.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PostServiceTest {

    @Autowired
    PostService postService;

//    @Test
//    public void post추가() throws Exception {
//
//        // Given
//        User user = generateUser(1);
//        Article article = generateArticle();
//        Category category = new Category(1L, "", "");
//        Post post = Post.createPost(article, CommentStatus.SHOW, user, category);
//
//        // When
//        Long postId = postService.add(post);
//
//        // Then
//        assertEquals(post, postService.findOne(postId));
//    }
//
//    @Test
//    public void post_전부_조회() throws Exception {
//
//        // Given
//        List<Post> posts = new ArrayList<>();
//        for (int i = 0; i < 30; ++i) {
//            posts.add(generatePost(i));
//        }
//
//        // When
//        for(Post post : posts) {
//            postService.add(post);
//        }
//
//        // Then
//        assertEquals(posts.size(), postService.findAll().size());
//    }
//
//    @Test
//    public void post_status_변경() throws Exception {
//
//        // Given
//        User user = generateUser(1);
//        Article article = generateArticle();
//        Category category = new Category(1L, "", "");
//        Post prePost = Post.createPost(article, CommentStatus.SHOW, user, category);
//
//        postService.add(prePost);
//
//        // When
//        prePost.changeStatus();
//
//        // Then
//        assertEquals(prePost.getStatus(), CommentStatus.HIDE);
//    }
//
//    private Post generatePost(int index) {
//        User user = generateUser(index);
//        Article article = generateArticle();
//        Category category = new Category(Long.valueOf(index), "", "");
//        return Post.createPost(article, CommentStatus.SHOW, user, category);
//    };
//
//    private User generateUser(int index) {
//        User user = User.createUser(
//                index + "user@email.com",
//                "password",
//                "nickname"
//        );
//        user.setId(Long.valueOf(index));
//        return user;
//    }

}