package com.master.side.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "member", uniqueConstraints = {
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "username", nullable = false, length = 255)
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

    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private Timestamp updatedAt;

    @Column(length = 255)
    private String address;

    // 연관관계: 한 명의 Member가 여러 Board를 작성할 수 있음
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Board> boards;

    // 연관관계: 한 명의 Member가 여러 Comment를 작성할 수 있음
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Comment> comments;

    // 연관관계: 한 명의 Member가 여러 Notification을 가질 수 있음
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Notification> notifications;

    // 연관관계: 한 명의 Member가 여러 Task를 가질 수 있음
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Task> tasks;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(this.getRoles().toArray(new String[0]))
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}