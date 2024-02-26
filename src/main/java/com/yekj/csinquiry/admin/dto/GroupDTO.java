package com.yekj.csinquiry.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupDTO {
    private String id;
    private String name;
    private String description;
    private int userCount;
}
