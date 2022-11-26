package com.geeks.geeksbackend.service;

import com.geeks.geeksbackend.entity.Member;
import com.geeks.geeksbackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String name) {
        return userRepository.findOneWithAuthoritiesByName(name)
                .map(user -> createUser(name, user))
                .orElseThrow(() -> new UsernameNotFoundException(name + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private org.springframework.security.core.userdetails.User createUser(String username, Member member) {
        List<GrantedAuthority> grantedAuthorities = member.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(member.getName(),
                member.getPassword(),
                grantedAuthorities);
    }
}