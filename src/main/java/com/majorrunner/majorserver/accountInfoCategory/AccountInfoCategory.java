package com.majorrunner.majorserver.accountInfoCategory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.majorrunner.majorserver.accoutInfo.AccountInfo;
import com.majorrunner.majorserver.category.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfoCategory {

    @Id @GeneratedValue
    @Column(name = "account_info_category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_info_id")
    @JsonIgnore
    private AccountInfo accountInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category category;

    public AccountInfoCategory(AccountInfo accountInfo, Category category) {
        this.accountInfo = accountInfo;
        this.category = category;
    }
}
