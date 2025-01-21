package com.master.side.domain.model;

import java.io.Serializable;
import java.util.Objects;

public class MemberRoleId implements Serializable {

    private Long memberId;
    private Long roleId;

    public MemberRoleId() {
    }

    public MemberRoleId(Long memberId, Long roleId) {
        this.memberId = memberId;
        this.roleId = roleId;
    }

    // equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberRoleId)) return false;
        MemberRoleId that = (MemberRoleId) o;
        return Objects.equals(memberId, that.memberId) &&
                Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, roleId);
    }
}