package com.master.side.domain.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class MemberRoleId implements Serializable {

    private UUID memberId;
    private UUID roleId;

    public MemberRoleId() {
    }

    public MemberRoleId(UUID memberId, UUID roleId) {
        this.memberId = memberId;
        this.roleId = roleId;
    }

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
