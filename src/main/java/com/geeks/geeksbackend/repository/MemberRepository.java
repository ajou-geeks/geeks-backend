package com.geeks.geeksbackend.repository;

import com.geeks.geeksbackend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
