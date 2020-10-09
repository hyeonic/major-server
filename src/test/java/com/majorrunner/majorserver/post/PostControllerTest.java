package com.majorrunner.majorserver.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.majorrunner.majorserver.account.*;
import com.majorrunner.majorserver.category.Category;
import com.majorrunner.majorserver.category.CategoryRepository;
import com.majorrunner.majorserver.comment.CommentDto;
import com.majorrunner.majorserver.comment.CommentStatus;
import com.majorrunner.majorserver.common.AppProperties;
import com.majorrunner.majorserver.common.TestDecription;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class PostControllerTest {

    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @Autowired ModelMapper modelMapper;
    @Autowired AccountRepository accountRepository;
    @Autowired AccountService accountService;
    @Autowired CategoryRepository categoryRepository;
    @Autowired PostRepository postRepository;
    @Autowired PostService postService;
    @Autowired PostController postController;
    @Autowired AppProperties appProperties;

    @Before
    public void setUp() {
        this.postRepository.deleteAll();
        this.categoryRepository.deleteAll();
    }

    private String getBearerToken() throws Exception {
        return "Bearer " + getAccessToken();
    }

    private String getAccessToken() throws Exception {

        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
                .param("username", "test@email.com")
                .param("password","1234")
                .param("grant_type", "password"));

        String responseBody = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();

        return parser.parseMap(responseBody).get("access_token").toString();

    }

    @Test
    @TestDecription("정상적으로 post를 생성하는 테스트")
    public void createPost() throws Exception {

        // Given
        generateAccount();
        Optional<Account> optionalAccount = accountRepository.findByUsername("test@email.com");
        Account account = optionalAccount.get();
        Category category = generateCategory();

        PostDto.CreatePostRequest postDto = PostDto.CreatePostRequest.builder()
                .title("post test")
                .contents("post test를 위한 contents입니다. ")
                .commentStatus(CommentStatus.SHOW)
                .account(account)
                .category(category)
                .build();

        // When & Then
        mockMvc.perform(post("/api/posts")
                    .header(HttpHeaders.AUTHORIZATION, getBearerToken())
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
        generateAccount();
        Optional<Account> optionalAccount = accountRepository.findByUsername("test@email.com");
        Account account = optionalAccount.get();
        Category category = generateCategory();

        PostDto.CreatePostRequest postDto = PostDto.CreatePostRequest.builder()
                .contents("post test를 위한 contents입니다. ")
                .commentStatus(CommentStatus.SHOW)
                .account(account)
                .category(category)
                .build();

        // When & Then
        mockMvc.perform(post("/api/posts")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(postDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDecription("post 검색 결과를 조회하는 테스트")
    public void searchPosts() throws Exception {

        // Given
        Category category1 = generateCategory();
        Category category2 = generateCategory2();
        generateAccount();

        Optional<Account> optionalAccount = accountRepository.findByUsername("test@email.com");
        Account account = optionalAccount.get();

        for (int i = 0; i < 15; i++) {
            generatePost(i, account, category1);
        }

        for (int i = 0; i < 15; i++) {
            generatePost(i, account, category2);
        }

        // When & Then
        mockMvc.perform(get("/api/posts/search/{title}", "2번")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "createdAt,DESC"))
                .andDo(print());

    }

    @Test
    @TestDecription("post 검색 결과가 없는 테스트")
    public void searchPosts_204() throws Exception {

        // Given
        Category category1 = generateCategory();
        Category category2 = generateCategory2();
        generateAccount();

        Optional<Account> optionalAccount = accountRepository.findByUsername("test@email.com");
        Account account = optionalAccount.get();

        for (int i = 0; i < 15; i++) {
            generatePost(i, account, category1);
        }

        for (int i = 0; i < 15; i++) {
            generatePost(i, account, category2);
        }

        // When & Then
        mockMvc.perform(get("/api/posts/search/{title}", "400번")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "createdAt,DESC"))
                .andDo(print())
                .andExpect(status().isNoContent());

    }

    @Test
    @TestDecription("정상적인 post를 한 건 조회하는 테스트")
    public void post_한건_조회() throws Exception {

        // Given
        Account account = generateAccount();
        Category category = generateCategory();

        Post generatePost = generatePost(1, account, category);

        // When & Then
        mockMvc.perform(get("/api/posts/{id}", generatePost.getId()))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @TestDecription("없는 post를 한 건 조회하는 테스트")
    public void post_한건_조회_404() throws Exception {

        // Given
        Account account = generateAccount();
        Category category = generateCategory();

        Post generatePost = generatePost(1, account, category);

        // When & Then
        mockMvc.perform(get("/api/posts/404"))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    @TestDecription("post를 수정하는 테스트")
    public void updatePost() throws Exception {

        // Given
        Category category = generateCategory();
        generateAccount();
        Optional<Account> optionalAccount = accountRepository.findByUsername("test@email.com");
        Account account = optionalAccount.get();

        Post generatePost = generatePost(1, account, category);
        PostDto.CreatePostRequest postDto = modelMapper.map(generatePost, PostDto.CreatePostRequest.class);
        postDto.setContents("수정되었습니다.");

        // When & Then
        mockMvc.perform(put("/api/posts/{id}", generatePost.getId())
                    .header(HttpHeaders.AUTHORIZATION, getBearerToken())
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
        Category category = generateCategory();
        generateAccount();
        Optional<Account> optionalAccount = accountRepository.findByUsername("test@email.com");
        Account account = optionalAccount.get();

        Post generatePost = generatePost(1, account, category);
        PostDto.CreatePostRequest postDto = modelMapper.map(generatePost, PostDto.CreatePostRequest.class);
        postDto.setContents("수정되었습니다.");

        // When & Then
        mockMvc.perform(put("/api/posts/404")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
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
        Category category = generateCategory();
        generateAccount();
        Optional<Account> optionalAccount = accountRepository.findByUsername("test@email.com");
        Account account = optionalAccount.get();

        Post generatePost = generatePost(1, account, category);

        // When & Then
        mockMvc.perform(delete("/api/posts/{id}", generatePost.getId())
                    .header(HttpHeaders.AUTHORIZATION, getBearerToken()))
                .andDo(print())
                .andExpect(status().isNoContent());

    }

    @Test
    @TestDecription("존재하는 post를 삭제하는 테스트")
    public void deletePost_404() throws Exception {

        // Given
        generateAccount();

        // When & Then
        mockMvc.perform(delete("/api/posts/404")
                    .header(HttpHeaders.AUTHORIZATION, getBearerToken()))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    @TestDecription("catgory별 post list를 출력하는 테스트")
    public void 카페고리별_queryPosts() throws Exception {

        // Given
        Category category1 = generateCategory();
        Category category2 = generateCategory2();
        generateAccount();

        Optional<Account> optionalAccount = accountRepository.findByUsername("test@email.com");
        Account account = optionalAccount.get();

        for (int i = 0; i < 15; i++) {
            generatePost(i, account, category1);
        }

        for (int i = 0; i < 15; i++) {
            generatePost(i, account, category2);
        }

        // When & Then
        mockMvc.perform(get("/api/posts/category/{id}", category1.getId())
                .param("page", "1")
                .param("size", "10")
                .param("sort", "createdAt,DESC"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @TestDecription("특정 post에 comment를 추가하는 테스트")
    public void createComment() throws Exception {

        // Given
        Category category = generateCategory();
        generateAccount();

        Optional<Account> optionalAccount = accountRepository.findByUsername("test@email.com");
        Account account = optionalAccount.get();
        Post generatePost = generatePost(1, account, category);

        CommentDto.CreateCommentRequest createCommentRequest = CommentDto.CreateCommentRequest.builder()
                .comment("test comment")
                .account(account)
                .build();

        // When & Then
        mockMvc.perform(post("/api/posts/{id}/comment", generatePost.getId())
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .content(objectMapper.writeValueAsString(createCommentRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON))
        .andDo(print());

    }

    private Post generatePost(int index, Account account, Category category) {

        Post post = Post.createPost(
                index + "번 게시물",
                "test contents",
                CommentStatus.SHOW,
                account,
                category
        );

        Long id = postService.add(post);
        return postService.findOne(id);
    }

    private Account generateAccount() {
        AccountDto.CreateAccountRequest userDto =
                new AccountDto.CreateAccountRequest("test@email.com", "1234", "test");
        Account account = modelMapper.map(userDto, Account.class);
        Account savedAccount = accountService.saveAccount(account);

        return savedAccount;
    }

    private Category generateCategory() {
        Category category = new Category(1L,"전공", "it");
        categoryRepository.save(category);

        return category;
    }

    private Category generateCategory2() {
        Category category = new Category(2L,"전공", "소프트웨어");
        categoryRepository.save(category);

        return category;
    }
}