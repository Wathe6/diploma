package com.envoi.diploma.repository;

import com.envoi.diploma.model.ExtendedGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtendedGroupRepository extends JpaRepository<ExtendedGroup, String> {
}
