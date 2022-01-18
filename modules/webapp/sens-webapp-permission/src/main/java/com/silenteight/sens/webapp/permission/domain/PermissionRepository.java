package com.silenteight.sens.webapp.permission.domain;

import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.UUID;

interface PermissionRepository extends Repository<Permission, UUID> {

  Collection<Permission> findAll();
}
