package com.majorrunner.majorserver.userInfo;

import com.majorrunner.majorserver.category.Category;
import com.majorrunner.majorserver.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfo {

    @Id @GeneratedValue
    @Column(name = "user_info_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany
    private List<Category> categories = new ArrayList<>();

    // == 연관관계 메서드 == //
    public void addCategory(Category category) {
        categories.add(category);
    }

    // == 생성 메서드 == //
    // 정적 팩토리 메서드
    public static UserInfo createUserInfo(User user, Category... categories) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUser(user);
        for (Category category : categories) {
           userInfo.addCategory(category);
        }

        return userInfo;
    }
}
