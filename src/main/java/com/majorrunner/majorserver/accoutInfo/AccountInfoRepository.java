package com.majorrunner.majorserver.accoutInfo;

import com.majorrunner.majorserver.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AccountInfoRepository extends JpaRepository<AccountInfo, Long> {

    @Transactional
    void deleteByAccount(Account account);
}
