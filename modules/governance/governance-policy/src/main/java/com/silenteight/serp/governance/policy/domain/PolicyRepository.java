package com.silenteight.serp.governance.policy.domain;

import org.springframework.data.repository.Repository;

interface PolicyRepository extends Repository<Policy, Long> {

  Policy save(Policy policy);
}
