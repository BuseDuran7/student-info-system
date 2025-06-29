package com.ege.security;

import com.ege.entities.User;
import com.ege.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("🔍 Loading user by username: " + username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("❌ User not found: " + username);
                    return new UsernameNotFoundException("User not found with username: " + username);
                });

        System.out.println("✅ User found: " + user.getUsername());
        System.out.println("🔐 Password hash: " + user.getPassword().substring(0, 10) + "...");
        System.out.println("👤 User active: " + user.getIsActive());

        // UserRole null kontrolü
        if (user.getUserRole() == null) {
            System.out.println("❌ UserRole is NULL for user: " + username);
            throw new UsernameNotFoundException("User role not found for user: " + username);
        }

        System.out.println("🏷️ User role: " + user.getUserRole().getRoleName());
        System.out.println("🟢 Role active: " + user.getUserRole().getIsActive());

        return createUserPrincipal(user);
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long id) {
        System.out.println("🔍 Loading user by ID: " + id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    System.out.println("❌ User not found with ID: " + id);
                    return new UsernameNotFoundException("User not found with id: " + id);
                });

        return createUserPrincipal(user);
    }

    private UserDetails createUserPrincipal(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Null kontrolü
        if (user.getUserRole() != null && user.getUserRole().getRoleName() != null) {
            String roleName = user.getUserRole().getRoleName();
            // ROLE_ prefix'i ekle (Spring Security standardı)
            authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
            System.out.println("🔑 Authority added: ROLE_" + roleName);
        } else {
            System.out.println("⚠️ No role found for user: " + user.getUsername());
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getIsActive() != null ? user.getIsActive() : false,
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities
        );

        System.out.println("✅ UserDetails created successfully for: " + user.getUsername());
        return userDetails;
    }
}