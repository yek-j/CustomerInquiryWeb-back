package com.yekj.csinquiry.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserAuthDTO {
    private String email;
    private String groupId;
    private String groupName;
    private String admin;
}
