package com.majorrunner.majorserver.accoutInfo;

import com.majorrunner.majorserver.account.Account;
import com.majorrunner.majorserver.category.Category;
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
public class AccountInfo {

    @Id @GeneratedValue
    @Column(name = "account_info_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany
    private List<Category> categories = new ArrayList<>();

    // == 연관관계 메서드 == //
    public void addCategory(Category category) {
        categories.add(category);
    }

    // == 생성 메서드 == //
    // 정적 팩토리 메서드
    public static AccountInfo createUserInfo(Account account, List<Category> categories) {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccount(account);
        for (Category category : categories) {
           accountInfo.addCategory(category);
        }

        return accountInfo;
    }
}
