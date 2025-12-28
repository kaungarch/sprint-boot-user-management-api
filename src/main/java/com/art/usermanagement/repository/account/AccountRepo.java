package com.art.usermanagement.repository.account;

import com.art.usermanagement.model.Account;
import com.art.usermanagement.model.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepo extends JpaRepository<Account, UUID> {
//    @Query(
//            "SELECT a FROM Account a WHERE a.phone_number=?1 OR a.nrc_number=?2"
//    )
    Optional<Account> findFirstByPhoneNumberOrNrcNumber(String phoneNumber, String nrcNumber);

    Optional<Account> findByIdAndStatus(UUID id, AccountStatus userStatus);

    Optional<Account> findByPhoneNumber(String phoneNumber);
}
