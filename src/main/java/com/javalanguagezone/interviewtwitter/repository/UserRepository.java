package com.javalanguagezone.interviewtwitter.repository;

import com.javalanguagezone.interviewtwitter.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findOneByUsername(String user);
}
