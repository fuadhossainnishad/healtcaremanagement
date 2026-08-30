package com.healthcaremanagement.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "auth")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    private boolean enabled = false;          // set true after OTP verification

    private boolean phoneVerified = false;

    private boolean emailVerified = false;    // if we also verify email

    private boolean onboardingCompleted = false;

    private Integer onboardingStep = 0;       // 0=not started, 1=gender, 2=dob, 3=location

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "auth_roles",
        joinColumns = @JoinColumn(name = "auth_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}