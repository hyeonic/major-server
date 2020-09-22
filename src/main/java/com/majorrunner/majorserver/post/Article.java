package com.majorrunner.majorserver.post;

import com.sun.xml.internal.ws.api.ha.StickyFeature;
import lombok.Getter;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
@Getter
public class Article {

    private String title;
    private String contents;
    private String filePath; // 좀 더 찾아 볼 것

    protected Article() {
    }

    public Article(String title, String contents, String filePath) {
        this.title = title;
        this.contents = contents;
        this.filePath = filePath;
    }
}
