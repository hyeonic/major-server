package com.majorrunner.majorserver.Like;

import com.majorrunner.majorserver.post.Post;
import com.majorrunner.majorserver.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/like", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class LikeController {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    @GetMapping("/post/{postId}/nick-name/{nickName}")
    public ResponseEntity getLike(@PathVariable(name="postId") Long postId,
                                  @PathVariable(name="nickName") String ncikName) {

        Optional<Post> optionalPost = postRepository.findById(postId);

        if (!optionalPost.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Post post = optionalPost.get();

        List<Like> likes = likeRepository.findByPost(post);

        Map result = new HashMap();
        for (Like like : likes) {
            if (like.getAccount().getNickName().equals(ncikName)) {
                result.put("result", true);
                return ResponseEntity.ok().body(result);
            }
        }

        result.clear();

        result.put("result", false);
        return ResponseEntity.ok().body(result);
    }
}
