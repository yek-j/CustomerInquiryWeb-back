package com.yekj.csinquiry.admin.service;

import com.yekj.csinquiry.admin.dto.GroupDTO;
import com.yekj.csinquiry.admin.dto.UserAuthDTO;
import com.yekj.csinquiry.admin.exception.GroupAlreadyExistsException;
import com.yekj.csinquiry.user.entity.Group;
import com.yekj.csinquiry.user.entity.User;
import com.yekj.csinquiry.user.repository.GroupRepository;
import com.yekj.csinquiry.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
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

    public List<GroupDTO> getGroupList() {
        List<Group> findGroupList = groupRepository.findAll();

        List<GroupDTO> gruopList = findGroupList.stream()
                .map(group ->{
                    GroupDTO groupDto = new GroupDTO();
                    groupDto.setId(group.getId().toString());
                    groupDto.setName(group.getName());
                    return groupDto;
                }).toList();

        return gruopList;
    }

    public GroupDTO getGroupInfo(String id) {
        Optional<Group> group = groupRepository.findById(Long.valueOf(id));
        GroupDTO groupDTO = new GroupDTO();

        if(group.isPresent()) {
            groupDTO.setId(id);
            groupDTO.setName(group.get().getName());
            groupDTO.setDescription(group.get().getDescription());
            groupDTO.setUserCount(group.get().getUsers().size());
        }

        return groupDTO;
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

    @Transactional
    public void setUserAuth(UserAuthDTO newUserGroup) throws Exception {
        Optional<User> user = userRepository.findUserByEmail(newUserGroup.getEmail());
        Group group = new Group();
        if(newUserGroup.getGroupId().isEmpty()) group = null;
        else group = groupRepository.getReferenceById(Long.valueOf(newUserGroup.getGroupId()));

        if(user.isPresent()) {
            user.get().setGroup(group);
            user.get().setAdmin(newUserGroup.getAdmin().equals("admin") ? "Y" : "N");
        }
    }

    @Transactional
    public List<GroupDTO> addGroup(String name, String description) throws Exception {
        Group newGroup = new Group();
        newGroup.setName(name);
        newGroup.setDescription(description);

        groupRepository.save(newGroup);

        List<GroupDTO> newGroupList = getGroupList();

        return newGroupList;
    }

    @Transactional
    public List<GroupDTO> updateGroup(String id, String name, String description) throws Exception {
        Optional<Group> updateGroup = groupRepository.findById(Long.valueOf(id));
        if (updateGroup.isPresent()) {
            updateGroup.get().setName(name);
            updateGroup.get().setDescription(description);
        } else {
            throw new EntityNotFoundException();
        }

        List<GroupDTO> updateGroupList = getGroupList();

        return updateGroupList;
    }

    @Transactional
    public List<GroupDTO> deleteGroup(String id) throws Exception {
        Optional<Group> group = groupRepository.findById(Long.valueOf(id));
        int userCount = group.get().getUsers().size();
        if(userCount > 0) {
            throw new GroupAlreadyExistsException("사용자가 있으면 삭제 불가능");
        }

        groupRepository.deleteById(Long.valueOf(id));
        List<GroupDTO> deleteGroupList = getGroupList();

        return deleteGroupList;
    }
}
