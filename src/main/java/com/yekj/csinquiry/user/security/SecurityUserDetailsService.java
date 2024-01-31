package com.yekj.csinquiry.user.security;

import com.yekj.csinquiry.user.entity.User;
import com.yekj.csinquiry.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByEmail(email);

        if(user.isPresent()) {
            return new SecurityUserDetails(user.get());
        } else {
            throw new UsernameNotFoundException("가입된 이메일이 아닙니다.");
        }
    }
}
