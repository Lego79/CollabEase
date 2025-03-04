package com.master.side.domain.repository;

import com.master.side.domain.model.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends CrudRepository<Member, UUID> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByUsername(String username);

    List<Member> findTop20ByUsernameContainingIgnoreCase(String username);
}
