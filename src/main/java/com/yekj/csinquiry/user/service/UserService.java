package com.yekj.csinquiry.user.service;

import com.yekj.csinquiry.config.JwtProvider;
import com.yekj.csinquiry.user.dto.UserDTO;
import com.yekj.csinquiry.user.entity.User;
import com.yekj.csinquiry.user.repository.UserRepository;
import io.jsonwebtoken.security.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO getUserInfo(String token) throws Exception {
        Long uid = jwtProvider.getSubject(token);

        Optional<User> user = userRepository.findById(uid);
        UserDTO userDTO = new UserDTO();

        if(user.isPresent()){
            userDTO.setEmail(user.get().getEmail());
            userDTO.setName(user.get().getName());
            userDTO.setPassword("");
        } else {
            throw new Exception("사용자가 존재하지 않습니다.");
        }

        return userDTO;
    }

    @Transactional
    public void updateUserInfo(UserDTO user, String token) throws Exception {
        Optional<User> updateUser = userRepository.findUserByEmail(user.getEmail());

        if(updateUser.isPresent()) {
            Long uid = jwtProvider.getSubject(token);
            if(!Objects.equals(uid,updateUser.get().getId())) {
                throw new Exception("권한없는 사용자는 사용자 정보 수정 불가능");
            }

            if(!user.getName().isEmpty()) {
                updateUser.get().setName(user.getName());
            } else {
                throw new Exception("이름은 필수값입니다.");
            }

            if(!user.getPassword().isEmpty()) {
                updateUser.get().setPassword(passwordEncoder.encode(user.getPassword()));
            }

            userRepository.save(updateUser.get());
        } else {
            throw new Exception("존재하지 않는 사용자입니다.");
        }
    }
}
