package com.master.side.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "member_roles")
@IdClass(MemberRoleId.class)
public class MemberRole {

    @Id
    @Column(name = "member_id")
    private Long memberId;

    @Id
    @Column(name = "role_id")
    private Long roleId;

    // Additional foreign key reference to member (member_member_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_member_id", nullable = false, foreignKey = @ForeignKey(name = "fkruptm2dtwl95mfks4bnhv828k"))
    private Member memberMember;

    @Column(name = "roles", length = 255)
    private String roles;

    // Many-to-One to Member via member_id for logical association
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_member_roles_member"))
    private Member member;

    // Many-to-One to Role
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_member_roles_role"))
    private Role role;

    public MemberRole() {
    }

    // Getters and setters
    // ... (omitted for brevity)
}