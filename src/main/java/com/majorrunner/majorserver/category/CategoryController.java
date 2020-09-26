package com.majorrunner.majorserver.category;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/category", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity getPosts() {

        List<Category> categories = categoryRepository.findAll();

        return ResponseEntity.ok().body(categories);
    }
}
