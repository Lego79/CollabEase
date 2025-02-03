package com.master.side.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "member_roles")
@IdClass(MemberRoleId.class)
@Getter
@Setter
@AllArgsConstructor
@Builder
public class MemberRole {

    @Id
    @Column(name = "member_id")
    private UUID memberId;

    @Id
    @Column(name = "role_id")
    private UUID roleId;

    @Column(name = "roles", length = 255)
    private String roles;

    // Many-to-One to Member via member_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_member_roles_member"))
    private Member member;

    // Many-to-One to Role via role_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_member_roles_role"))
    private Role role;

    public MemberRole() {
    }
}
