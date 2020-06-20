package com.olympus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.olympus.data.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

}
