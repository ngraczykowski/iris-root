package com.silenteight.serp.governance.policy.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.serp.governance.policy.PolicyModule;
import com.silenteight.serp.governance.policy.domain.PolicyRepositoryIT.PolicyRepositoryTestConfiguration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;

@TestPropertySource("classpath:data-test.properties")
@ContextConfiguration(classes = { PolicyRepositoryTestConfiguration.class })
class PolicyRepositoryIT extends BaseDataJpaTest {

  @Autowired
  private PolicyRepository underTest;

  @Test
  void savePolicy() {
    UUID policyId = fromString("01256804-1ce1-4d52-94d4-d1876910f272");
    String name = "policy-name";
    String createdBy = "asmith";
    Policy policy = new Policy(policyId, name, createdBy);
    Policy savedPolicy = underTest.save(policy);

    assertThat(savedPolicy.getId()).isNotNull();
    assertThat(savedPolicy.getPolicyId()).isEqualTo(policyId);
    assertThat(savedPolicy.getName()).isEqualTo(name);
    assertThat(savedPolicy.getCreatedBy()).isEqualTo(createdBy);
    assertThat(savedPolicy.getUpdatedBy()).isEqualTo(createdBy);
  }

  @Configuration
  @ComponentScan(basePackageClasses = {
      PolicyModule.class
  })
  static class PolicyRepositoryTestConfiguration {

  }
}
