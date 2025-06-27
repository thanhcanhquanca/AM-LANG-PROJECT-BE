package com.example.amlang.security;

import com.example.amlang.entity.Permission;
import com.example.amlang.entity.User;
import com.example.amlang.repository.PermissionRepository;
import com.example.amlang.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));

        // Tải danh sách quyền
        List<String> permissions = permissionRepository.findByRoleRoleIdAndIsAllowedTrue(user.getRole().getRoleId())
                .stream()
                .map(Permission::getPermissionName)
                .collect(Collectors.toList());

        List<org.springframework.security.core.authority.SimpleGrantedAuthority> authorities = permissions.stream()
                .map(org.springframework.security.core.authority.SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getPhoneNumber(),
                user.getPassword(),
                authorities
        );
    }
}