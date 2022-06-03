package com.silenteight.serp.governance.policy.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

class InMemoryPolicyRepository extends BasicInMemoryRepository<Policy> implements PolicyRepository {

  @Override
  public Policy save(Policy policy) {
    if (policy.getId() == null)
      Optional.ofNullable(policy.getPolicyId())
          .flatMap(this::findByPolicyId)
          .ifPresent(
              exists -> {
                throw new DuplicateKeyException(
                    "policy with policy id "
                        + policy.getPolicyId()
                        + " already exists with id "
                        + exists.getId());
              });

    return super.save(policy);
  }

  @Override
  public Optional<Policy> findByPolicyId(UUID policyId) {
    var policies = stream().filter(policy -> policy.getPolicyId() == policyId).toList();
    if (policies.size() > 1) {
      throw new DataIntegrityViolationException(
          "query returned non unique results: " + policies.size());
    }
    return policies.stream().findAny();
  }

  @Override
  public Policy getByPolicyId(UUID policyId) {
    return findByPolicyId(policyId).orElseThrow();
  }

  @Override
  public Collection<Policy> findAll() {
    return stream().collect(toList());
  }

  @Override
  public Collection<Policy> findAllByStateIn(Collection<PolicyState> states) {
    return stream().filter(policy -> states.contains(policy.getState())).collect(toList());
  }

  @Override
  public Long getIdByPolicyId(UUID policyId) {
    return getByPolicyId(policyId).getId();
  }

  @Override
  public Optional<Policy> findByStateEquals(PolicyState policyState) {
    return Optional.empty();
  }

  @Override
  public Policy getById(Long policyId) {
    return stream().filter(policy -> policy.getId().equals(policyId)).findFirst().orElseThrow();
  }

  @Override
  public void deleteByPolicyId(UUID policyId) {
    Policy policyToDelete =
        stream().filter(policy -> policy.hasPolicyId(policyId)).findAny().orElseThrow();
    delete(policyToDelete);
  }

  @Override
  public String getPolicyName(UUID policyId) {
    return getByPolicyId(policyId).getName();
  }
}
