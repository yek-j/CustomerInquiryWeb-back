package com.yekj.csinquiry.user.service;

import com.yekj.csinquiry.user.dto.UserDTO;
import com.yekj.csinquiry.user.entity.User;
import com.yekj.csinquiry.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class SignupService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean addUser(UserDTO userDTO) {
        Optional<User> findUser = userRepository.findUserByEmail(userDTO.getEmail());

        if(findUser.isPresent()) { // 이미 존재하는 User
            return false;
        } else {
            User newUser = new User();
            newUser.setEmail(userDTO.getEmail());
            newUser.setName(userDTO.getName());
            newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            userRepository.save(newUser);
            return true;
        }
    }
}
