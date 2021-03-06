package com.majorrunner.majorserver.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.majorrunner.majorserver.accountInfoCategory.AccountInfoCategory;
import com.majorrunner.majorserver.post.Post;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED) // 생성자를 protected로 두어 생성을 막아둠
//@Builder
public class Category {

    @Id
    @Column(name = "category_id")
    private Long id;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Post> posts = new ArrayList<>();

    private String categoryName;
    private String subCategoryName;

    // test 용
    public Category(Long id, String categoryName, String subCategoryName) {
        this.id = id;
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
    }

    public Category(String categoryName, String subCategoryName) {
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
    }

    public Category() {
    }
}
