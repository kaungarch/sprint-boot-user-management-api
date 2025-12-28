package com.art.usermanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "T_BLACKLIST",
        uniqueConstraints = @UniqueConstraint(columnNames = {"phone_number", "nrc_number"})
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Blacklist extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @Column(name = "nrc_number", nullable = false)
    private String nrcNumber;

    @ManyToOne()
    @JoinColumn(
            name = "admin_id",
            nullable = false
    )
    private Account account;
}
