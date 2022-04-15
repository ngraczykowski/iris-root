package com.silenteight.serp.governance.policy.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;
import com.silenteight.serp.governance.policy.domain.dto.SavePolicyRequest;
import com.silenteight.serp.governance.policy.domain.dto.TransferredPolicyRootDto;
import com.silenteight.serp.governance.policy.domain.dto.UpdatePolicyRequest;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;

import static com.silenteight.serp.governance.policy.domain.PolicyState.DRAFT;
import static com.silenteight.serp.governance.policy.domain.PolicyState.SAVED;
import static java.time.Instant.now;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

@Transactional
@TestPropertySource("classpath:data-test.properties")
@ContextConfiguration(classes = { PolicyRepositoryTestConfiguration.class })
class PolicyQueryTest extends BaseDataJpaTest {

  private static final UUID FIRST_POLICY_UID = UUID.randomUUID();
  private static final String FIRST_POLICY_NAME = "POLICY_1";
  private static final String FIRST_POLICY_DESC = "FIRST_DESC";
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
    Collection<PolicyDto> result = underTest.list(of(SAVED));

    assertThat(result).isEmpty();
  }

  @Test
  void listSavedShouldReturnSavedPolicies_whenSavedInDb() {
    Policy saved = createSavedPolicy(
        FIRST_POLICY_UID, FIRST_POLICY_NAME, FIRST_POLICY_DESC, FIRST_POLICY_CREATED_BY);
    createDraftPolicy(SECOND_POLICY_UID, SECOND_POLICY_NAME, null, SECOND_POLICY_CREATED_BY);

    Collection<PolicyDto> result = underTest.list(of(SAVED));

    assertThat(result).containsOnly(saved.toDto());
  }

  @Test
  void getPolicyIdByIdShouldReturnPolicyId_whenSavedInDb() {
    Policy saved = createSavedPolicy(
        FIRST_POLICY_UID, FIRST_POLICY_NAME, FIRST_POLICY_DESC, FIRST_POLICY_CREATED_BY);
    createDraftPolicy(SECOND_POLICY_UID, SECOND_POLICY_NAME, null, SECOND_POLICY_CREATED_BY);

    UUID policyId = underTest.getPolicyIdById(saved.getId());

    assertThat(policyId).isEqualTo(FIRST_POLICY_UID);
  }

  @Test
  void listSavedAndDraftedShouldReturnSavedAndDraftedPolicies_whenSavedInDb() {
    Policy saved = createSavedPolicy(
        FIRST_POLICY_UID, FIRST_POLICY_NAME, FIRST_POLICY_DESC, FIRST_POLICY_CREATED_BY);
    Policy draft = createDraftPolicy(
        SECOND_POLICY_UID, SECOND_POLICY_NAME, null, SECOND_POLICY_CREATED_BY);

    Collection<PolicyDto> result = underTest.list(of(SAVED, DRAFT));

    assertThat(result).containsExactlyInAnyOrder(saved.toDto(), draft.toDto());
  }

  @Test
  void exportPolicy() {
    Policy policy = createSavedPolicy(
        FIRST_POLICY_UID, FIRST_POLICY_NAME, FIRST_POLICY_DESC, FIRST_POLICY_CREATED_BY);

    TransferredPolicyRootDto transferablePolicy = underTest.getTransferablePolicy(FIRST_POLICY_UID);

    assertThat(transferablePolicy).isNotNull();
    assertThat(transferablePolicy.getCreatedBy()).isEqualTo(FIRST_POLICY_CREATED_BY);
    assertThat(transferablePolicy.getMetadata().getCreatedBy()).isEqualTo(FIRST_POLICY_CREATED_BY);
    assertThat(transferablePolicy.getMetadata().getUpdatedBy()).isEqualTo(policy.getUpdatedBy());
    assertThat(transferablePolicy.getMetadata().getExportedAt()).isBeforeOrEqualTo(now());
    assertThat(transferablePolicy.getMetadata().getCreatedAt())
        .isEqualTo(policy.getCreatedAt().toInstant());
    assertThat(transferablePolicy.getMetadata().getUpdatedAt())
        .isEqualTo(policy.getUpdatedAt().toInstant());
    assertThat(transferablePolicy.getPolicy().getPolicyId()).isEqualTo(FIRST_POLICY_UID);
    assertThat(transferablePolicy.getPolicy().getDescription()).isEqualTo(FIRST_POLICY_DESC);
    assertThat(transferablePolicy.getPolicy().getName()).isEqualTo(FIRST_POLICY_NAME);
    assertThat(transferablePolicy.getPolicy().getSteps()).isEmpty();
  }

  @NotNull
  private Policy createSavedPolicy(
      UUID uuid, String name, String description, String createdBy) {
    Policy result = createDraftPolicy(uuid, name, description, createdBy);
    policyService.savePolicy(SavePolicyRequest.of(uuid, createdBy));
    return result;
  }

  @NotNull
  private Policy createDraftPolicy(
      UUID policyId, String name, String description, String createdBy) {
    UUID uuid = policyService.createPolicy(policyId, name, createdBy);
    policyService.updatePolicy(UpdatePolicyRequest.of(policyId, null, description, createdBy));
    return policyRepository.getByPolicyId(uuid);
  }
}
