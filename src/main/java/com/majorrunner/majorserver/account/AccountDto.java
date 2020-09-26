package com.majorrunner.majorserver.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    @NotEmpty
    private String email;
    @NotEmpty
    private String nickName;

}
