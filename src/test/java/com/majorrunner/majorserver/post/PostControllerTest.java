package com.majorrunner.majorserver.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.majorrunner.majorserver.category.Category;
import com.majorrunner.majorserver.category.CategoryRepository;
import com.majorrunner.majorserver.comment.CommentStatus;
import com.majorrunner.majorserver.common.TestDecription;
import com.majorrunner.majorserver.user.User;
import com.majorrunner.majorserver.user.UserDto;
import com.majorrunner.majorserver.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    ModelMapper modelMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;

    @Autowired
    PostController postController;

    @Before
    public void setUp() {
        this.postRepository.deleteAll();
        this.userRepository.deleteAll();
        this.categoryRepository.deleteAll();
    }

    @Test
    @TestDecription("정상적으로 post를 생성하는 테스트")
    public void createPost() throws Exception {

        // Given
        User user = generateUser();
        Category category = generateCategory();

        PostDto postDto = PostDto.builder()
                .title("post test")
                .contents("post test를 위한 contents입니다. ")
                .commentStatus(CommentStatus.SHOW)
                .user(user)
                .category(category)
                .build();

        // When
        userRepository.save(user); // user 등록
        categoryRepository.save(category); // category 등록

        // Then
        mockMvc.perform(post("/api/posts")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(postDto)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @TestDecription("title이 비어있는 경우 에러를 유발하는 테스트")
    public void title이_비어있는_post() throws Exception {

        // Given
        User user = generateUser();
        Category category = generateCategory();

        PostDto postDto = PostDto.builder()
                .contents("post test를 위한 contents입니다. ")
                .commentStatus(CommentStatus.SHOW)
                .user(user)
                .category(category)
                .build();

        // When
        userRepository.save(user); // user 등록
        categoryRepository.save(category); // category 등록

        // Then
        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(postDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDecription("정상적인 post를 한 건 조회하는 테스트")
    public void post_한건_조회() throws Exception {

        // Given
        User user = generateUser();
        Category category = generateCategory();

        Post generatePost = generatePost(1, user, category);

        // When & Then
        mockMvc.perform(get("/api/posts/{id}", generatePost.getId()))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @TestDecription("없는 post를 한 건 조회하는 테스트")
    public void post_한건_조회_404() throws Exception {

        // Given
        User user = generateUser();
        Category category = generateCategory();

        Post generatePost = generatePost(1, user, category);

        // When & Then
        mockMvc.perform(get("/api/posts/404"))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    @TestDecription("post를 수정하는 테스트")
    public void updatePost() throws Exception {

        // Given
        User user = generateUser();
        Category category = generateCategory();

        Post generatePost = generatePost(1, user, category);

        PostDto postDto = modelMapper.map(generatePost, PostDto.class);

        postDto.setContents("수정되었습니다.");

        // When & Then
        mockMvc.perform(put("/api/posts/{id}", generatePost.getId())
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(postDto)))
                .andDo(print())
                .andExpect(status().isCreated());

    }

    @Test
    @TestDecription("존재 하지 않는 post를 수정하는 테스트")
    public void updatePost_404() throws Exception {

        // Given
        User user = generateUser();
        Category category = generateCategory();

        Post generatePost = generatePost(1, user, category);
        PostDto postDto = modelMapper.map(generatePost, PostDto.class);
        postDto.setContents("수정되었습니다.");

        // When & Then
        mockMvc.perform(put("/api/posts/404")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(postDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @TestDecription("존재하는 post를 삭제하는 테스트")
    public void deletePost() throws Exception {

        // Given
        User user = generateUser();
        Category category = generateCategory();

        Post generatePost = generatePost(1, user, category);

        // When & Then
        mockMvc.perform(delete("/api/posts/{id}", generatePost.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());

    }

    @Test
    @TestDecription("존재하는 post를 삭제하는 테스트")
    public void deletePost_404() throws Exception {

        // Given

        // When & Then
        mockMvc.perform(delete("/api/posts/404"))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    @TestDecription("post list를 출력하는 테스트")
    public void queryPosts() throws Exception {

        // Given
        User user = generateUser();
        Category category = generateCategory();
        userRepository.save(user);
        categoryRepository.save(category);

        for (int i = 0; i < 30; i++) {
            generatePost(i, user, category);
        }

        // When & Then
        mockMvc.perform(get("/api/posts")
                    .param("page", "1")
                    .param("size", "10")
                    .param("sort", "createdAt,DESC"))
                .andDo(print());

    }

    @Test
    @TestDecription("catgory별 post list를 출력하는 테스트")
    public void 카페고리별_queryPosts() throws Exception {
        
        // Given
        User user = generateUser();
        Category category1 = generateCategory();
        Category category2 = generateCategory2();
        userRepository.save(user);
        categoryRepository.save(category1);
        categoryRepository.save(category2);

        for (int i = 0; i < 15; i++) {
            generatePost(i, user, category1);
        }

        for (int i = 0; i < 15; i++) {
            generatePost(i, user, category2);
        }

        // When & Then
        mockMvc.perform(get("/api/posts/category/{id}", category1.getId())
                .param("page", "1")
                .param("size", "10")
                .param("sort", "createdAt,DESC"))
                .andDo(print());
        
    }


    private Post generatePost(int index, User user, Category category) {

        Post post = Post.createPost(
                index + "번 게시물",
                "test contents",
                CommentStatus.SHOW,
                user,
                category
        );

        Long id = postService.add(post);
        return postService.findOne(id);
    }

    private User generateUser() {
        UserDto userDto = new UserDto("user@email.com", "testuser");
        User user = modelMapper.map(userDto, User.class);

        return user;
    }

    private Category generateCategory() {
        Category category = new Category("전공", "it");

        return category;
    }

    private Category generateCategory2() {
        Category category = new Category("전공", "소프트웨어");

        return category;
    }
}