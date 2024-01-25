package com.yekj.csinquiry.user.repository;

import com.yekj.csinquiry.user.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
