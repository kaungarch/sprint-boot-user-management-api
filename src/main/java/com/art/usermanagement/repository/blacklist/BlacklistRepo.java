package com.art.usermanagement.repository.blacklist;

import com.art.usermanagement.model.Blacklist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BlacklistRepo extends JpaRepository<Blacklist, UUID> {
    boolean existsByPhoneNumberOrNrcNumber(String phoneNumber, String nrcNumber);

    @Query("SELECT bl FROM Blacklist bl")
    Page<Blacklist> filter(Pageable pageable);
}
