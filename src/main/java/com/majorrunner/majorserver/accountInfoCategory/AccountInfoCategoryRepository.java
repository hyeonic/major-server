package com.majorrunner.majorserver.accountInfoCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountInfoCategoryRepository extends JpaRepository<AccountInfoCategory, Long> {
}
