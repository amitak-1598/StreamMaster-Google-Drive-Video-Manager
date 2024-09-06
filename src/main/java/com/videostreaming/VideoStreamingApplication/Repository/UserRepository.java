package com.videostreaming.VideoStreamingApplication.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.videostreaming.VideoStreamingApplication.Entities.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);

}
