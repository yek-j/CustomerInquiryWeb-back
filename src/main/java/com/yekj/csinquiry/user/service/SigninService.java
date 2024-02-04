package com.yekj.csinquiry.user.service;

import com.yekj.csinquiry.config.JwtProvider;
import com.yekj.csinquiry.user.dto.Token;
import com.yekj.csinquiry.user.dto.UserDTO;
import com.yekj.csinquiry.user.entity.User;
import com.yekj.csinquiry.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SigninService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Autowired
    public SigninService(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }


    public Token Signin(UserDTO reqUser) {
        Optional<User> user = userRepository.findUserByEmail(reqUser.getEmail());

        if(!user.isPresent()) {
            throw new BadCredentialsException("로그인에 실패했습니다.");
        }

        if(!passwordEncoder.matches(reqUser.getPassword(), user.get().getPassword())) {
            throw new BadCredentialsException("로그인에 실패했습니다.");
        }

        return Token.builder()
                .name(user.get().getName())
                .token(jwtProvider.createJwt(user.get()))
                .build();
    }

}
