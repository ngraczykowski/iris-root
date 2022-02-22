package com.silenteight.sens.webapp.role.domain;

import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

interface RoleRepository extends Repository<Role, UUID> {

  Role save(Role role);

  Collection<Role> findAll();

  Optional<Role> findByRoleId(UUID roleId);

  void delete(Role role);
}
