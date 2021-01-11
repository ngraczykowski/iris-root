package com.silenteight.serp.governance.policy.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.UUID;

interface PolicyRepository extends Repository<Policy, Long> {

  Policy save(Policy policy);

  Policy getByPolicyId(UUID policyId);

  Collection<Policy> findAll();

  @Query("select p.id from Policy p where p.policyId = :policyId")
  Long getIdByPolicyId(UUID policyId);
}
