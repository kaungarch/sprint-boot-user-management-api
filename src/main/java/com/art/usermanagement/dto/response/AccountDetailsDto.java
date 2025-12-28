package com.art.usermanagement.dto.response;

import com.art.usermanagement.model.UserRole;
import com.art.usermanagement.model.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDetailsDto extends AccountDto{
        private UUID id;
        private String name;
        private String phoneNumber;
        private String nrcNumber;
        private AccountStatus status;
        private UserRole role;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

}
