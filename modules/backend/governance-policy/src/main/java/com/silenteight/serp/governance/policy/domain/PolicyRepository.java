package com.silenteight.serp.governance.policy.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

interface PolicyRepository extends Repository<Policy, Long> {

  Policy save(Policy policy);

  Optional<Policy> findByPolicyId(UUID policyId);

  Policy getByPolicyId(UUID policyId);

  Collection<Policy> findAll();

  Collection<Policy> findAllByStateIn(Collection<PolicyState> states);

  @Query("select p.id from Policy p where p.policyId = :policyId")
  Long getIdByPolicyId(UUID policyId);

  Optional<Policy> findByStateEquals(PolicyState policyState);

  Policy getById(Long policyId);

  void deleteByPolicyId(UUID policyId);

  @Query("select p.name from Policy p where p.policyId = :policyId")
  String getPolicyName(UUID policyId);
}
