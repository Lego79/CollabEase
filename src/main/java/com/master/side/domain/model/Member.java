package com.master.side.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(name = "member",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "nickname"),
                @UniqueConstraint(columnNames = "username")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "member_id")
    private UUID memberId;

    @Column(nullable = false, length = 255)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 255)
    private String nickname;

    @Column(length = 20)
    private String phone;

    @Column(name = "profile_img", length = 255)
    private String profileImg;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(name = "created_at", updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Timestamp createdAt;

    // 수정 시점을 자동으로 세팅 (Hibernate)
    @Column(name = "updated_at")
    @org.hibernate.annotations.UpdateTimestamp
    private Timestamp updatedAt;

    @Column(length = 255)
    private String address;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    // === 연관관계 설정 ===

    // Member ↔ Role : ManyToMany
    // (DDL에 member_roles 테이블이 있으나, "member_member_id"나 "roles" 컬럼이 추가로 존재)
    // 일반적인 ManyToMany라면 아래처럼 @JoinTable을 써도 되고,
    // 혹은 별도 엔티티(MemberRole)를 만들어 OneToMany/ManyToOne으로 풀어낼 수도 있습니다.
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "member_roles",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // One Member to Many Board
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards;

    // One Member to Many Task
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    // One Member to Many Comment
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    // One Member to Many Notification
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;

    // === Spring Security UserDetails 구현 ===
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 매핑 로직: RoleName을 SimpleGrantedAuthority 등으로 변환
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role r : this.roles) {
            authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority(r.getRoleName()));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked()  { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}
