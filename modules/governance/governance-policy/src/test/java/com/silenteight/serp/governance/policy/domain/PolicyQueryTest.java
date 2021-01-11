package com.silenteight.serp.governance.policy.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Collection;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@TestPropertySource("classpath:data-test.properties")
@ContextConfiguration(classes = { PolicyRepositoryTestConfiguration.class })
class PolicyQueryTest extends BaseDataJpaTest {

  private static final UUID FIRST_POLICY_UID = UUID.randomUUID();
  private static final String FIRST_POLICY_NAME = "POLICY_1";
  private static final String FIRST_POLICY_CREATED_BY = "USER_1";

  private static final UUID SECOND_POLICY_UID = UUID.randomUUID();
  private static final String SECOND_POLICY_NAME = "POLICY_2";
  private static final String SECOND_POLICY_CREATED_BY = "USER_2";

  private PolicyQuery underTest;

  @Autowired
  private PolicyService policyService;

  @Autowired
  private PolicyRepository policyRepository;

  @BeforeEach
  void setUp() {
    underTest = new PolicyDomainConfiguration().policyQuery(policyRepository);
  }

  @Test
  void listSavedShouldReturnEmpty_whenNothingIsSaved() {
    Collection<PolicyDto> result = underTest.listSaved();

    assertThat(result).isEmpty();
  }

  @Test
  void listSavedShouldReturnSavedPolicies_whenSavedInDb() {
    Policy firstSaved = policyService.addPolicy(
        FIRST_POLICY_UID, FIRST_POLICY_NAME, FIRST_POLICY_CREATED_BY);
    Policy secondSaved = policyService.addPolicy(
        SECOND_POLICY_UID, SECOND_POLICY_NAME, SECOND_POLICY_CREATED_BY);

    Collection<PolicyDto> result = underTest.listSaved();

    assertThat(result).contains(
        firstSaved.toDto(),
        secondSaved.toDto());
  }
}
