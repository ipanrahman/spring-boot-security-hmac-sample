package com.ipan97.springbootauditsample.repository.jpa;

import com.ipan97.springbootauditsample.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority,String> {
    Optional<Authority> findOneByName(String name);
}
