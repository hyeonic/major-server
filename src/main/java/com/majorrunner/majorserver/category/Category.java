package com.majorrunner.majorserver.category;

import com.majorrunner.majorserver.post.Post;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
public class Category {

    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "category")
    private List<Post> posts;

    private String categoryName;
    private String subCategoryName;

}
