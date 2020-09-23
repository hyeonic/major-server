package com.majorrunner.majorserver.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.majorrunner.majorserver.category.Category;
import com.majorrunner.majorserver.category.CategoryRepository;
import com.majorrunner.majorserver.comment.Comment;
import com.majorrunner.majorserver.comment.CommentStatus;
import com.majorrunner.majorserver.user.User;
import com.majorrunner.majorserver.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class PostControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PostService postService;

    @Autowired
    PostController postController;

    @Test
    public void createPost() throws Exception {

        // Given
        Post post = generatePost(1L, 1);

        // When
        Long postId = postService.add(post);

        // Then
        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(post)))
        .andDo(print())
//        .andExpect(jsonPath("id").exists())
        .andExpect(status().isCreated());
    }

    @Test
    public void queryPost() throws Exception {

        // Given
        User user = generateUser(1L);
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < 30; ++i) {
            posts.add(generatePost(1L, i));
        }

        // When
        userRepository.save(user);
        for (Post post : posts) {
            postService.add(post);
        }

        // Then
        mockMvc.perform(get("/api/posts")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "id,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
//        .andExpect(jsonPath("page").exists())
        ;
    }

    @Test
    public void post_한건_조회() throws Exception {

        // Given
        User user = generateUser(1L);
        Post post = generatePost(1L, 1);
        Comment comment = Comment.createComment("댓글", post);

        // When
        postService.savaComment(post, comment);
        postService.add(post);

        // Then
        mockMvc.perform(get("/api/posts/{id}", post.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    private Post generatePost(Long userId, int index) {
        User user = generateUser(userId);
        Article article = generateArticle();
        Category category = new Category(Long.valueOf(index), "", "");
        Post post = Post.createPost(article, CommentStatus.SHOW, user, category);
        return post;
    }

    private User generateUser(Long userId) {
        User user = User.createUser(
                Long.valueOf(userId) + "user@email.com",
                "password",
                "nickname"
        );
        user.setId(Long.valueOf(userId));
        return user;
    }

    private Article generateArticle() {
        return new Article(
                "title",
                "contents",
                ""
        );
    }
}