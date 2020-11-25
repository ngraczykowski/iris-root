package com.silenteight.serp.governance.branch;

import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@Disabled
@ContextConfiguration(classes = BranchModuleConfiguration.class)
class BranchRepositoryIT extends BaseDataJpaTest {

  @Autowired
  private BranchRepository repository;

  @Test
  void nothing() {

  }
}
