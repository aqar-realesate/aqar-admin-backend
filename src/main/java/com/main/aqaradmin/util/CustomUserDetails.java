package com.main.aqaradmin.util;

import com.main.aqaradmin.model.Admin;
import com.main.aqaradmin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin customer = adminRepository.findByEmail(email);
        if (customer == null) {
            throw new UsernameNotFoundException("Admin not found with email: " + email);
        }
        return User.builder()
                .username(customer.getEmail())
                .password(customer.getPassword())
                .build();
    }
}
