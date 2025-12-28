package com.art.usermanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "T_ACCOUNT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;
    @Column(name = "nrc_number", nullable = false, unique = true)
    private String nrcNumber;

    private String password;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(
            mappedBy = "account",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Project> projectList = List.of();

    @OneToMany(
            mappedBy = "account",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Blacklist> blacklists = List.of();

    public void addProject(Project project)
    {
        this.projectList.add(project);
        project.setAccount(this);
    }

    public void removeProject(Project project)
    {
        this.projectList.remove(project);
        project.setAccount(null);
    }
}