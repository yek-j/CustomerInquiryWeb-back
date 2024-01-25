package com.yekj.csinquiry.user.repository;

import com.yekj.csinquiry.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
