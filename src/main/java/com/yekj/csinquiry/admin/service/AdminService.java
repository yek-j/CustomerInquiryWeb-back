package com.yekj.csinquiry.admin.service;

import com.yekj.csinquiry.admin.dto.UserAuthDTO;
import com.yekj.csinquiry.user.entity.User;
import com.yekj.csinquiry.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdminService {
    @Autowired
    private UserRepository userRepository;

    public String[] getUserEmailList() {
        List<User> userList = userRepository.findAll();

        // email을 배열에 담고 return
        String[] arrUserEmail = userList.stream()
                .map(User::getEmail)
                .toArray(String[]::new);

        return arrUserEmail;
    }

    public UserAuthDTO getUserAuth(String email) {
        UserAuthDTO userAuthDTO = new UserAuthDTO(email, "", "", "user");
        Optional<User> user = userRepository.findUserByEmail(email);

        if(user.isPresent()) {
            if (user.get().getGroup() != null) {
                userAuthDTO.setGroupId(user.get().getGroup().getId().toString());
                userAuthDTO.setGroupName(user.get().getGroup().getName());
            }

            if(user.get().getAdmin() != null) {
                String admin = user.get().getAdmin().equals("Y") ? "admin" : "user";
                userAuthDTO.setAdmin(admin);
            }
        }
        return userAuthDTO;
    }

    public void setGroup() {

    }
}
