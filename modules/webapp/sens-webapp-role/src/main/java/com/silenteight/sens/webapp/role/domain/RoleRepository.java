package com.silenteight.sens.webapp.role.domain;

import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.UUID;

interface RoleRepository extends Repository<Role, UUID> {

  Collection<Role> findAll();
}
