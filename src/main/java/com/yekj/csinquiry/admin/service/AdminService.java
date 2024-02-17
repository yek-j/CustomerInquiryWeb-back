package com.yekj.csinquiry.admin.service;

import com.yekj.csinquiry.admin.dto.GroupListDTO;
import com.yekj.csinquiry.admin.dto.UserAuthDTO;
import com.yekj.csinquiry.user.entity.Group;
import com.yekj.csinquiry.user.entity.User;
import com.yekj.csinquiry.user.repository.GroupRepository;
import com.yekj.csinquiry.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class AdminService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    public String[] getUserEmailList() {
        List<User> userList = userRepository.findAll();

        // email을 배열에 담고 return
        String[] arrUserEmail = userList.stream()
                .map(User::getEmail)
                .toArray(String[]::new);

        return arrUserEmail;
    }

    public List<Map<String, String>> getSelectGroupList() {
        List<Group> findGroupList = groupRepository.findAll();

        List<Map<String, String>> gruopList = findGroupList.stream()
                .map(group ->{
                    Map<String, String> map = new HashMap<>();
                    map.put("id", group.getId().toString());
                    map.put("name", group.getName());

                    return map;
                }).toList();

        return gruopList;
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

    public void addGroup(String name, String description) {
        Group newGroup = new Group();
        newGroup.setName(name);
        newGroup.setDescription(description);

        groupRepository.save(newGroup);
    }
}
