package com.silenteight.serp.governance.model.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.serp.governance.model.ModelTestConfiguration;
import com.silenteight.serp.governance.model.domain.dto.ModelDto;
import com.silenteight.serp.governance.model.domain.exception.ModelMisconfiguredException;
import com.silenteight.serp.governance.policy.current.CurrentPolicyProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

import static com.silenteight.serp.governance.model.domain.ModelQuery.DEFAULT_MODEL_ID;
import static com.silenteight.serp.governance.model.domain.ModelQuery.DEFAULT_MODEL_NAME;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.MODEL_ID;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.MODEL_RESOURCE_NAME;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.POLICY_NAME;
import static com.silenteight.serp.governance.policy.current.CurrentPolicyFixture.CURRENT_POLICY_NAME;
import static java.time.OffsetDateTime.now;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
@Transactional
@ContextConfiguration(classes = { ModelTestConfiguration.class })
@TestPropertySource("classpath:data-test.properties")
@AutoConfigureJsonTesters
class ModelQueryTest extends BaseDataJpaTest {

  ModelQuery underTest;

  @Qualifier("modelRepository")
  @Autowired
  ModelRepository repository;

  @Mock
  private CurrentPolicyProvider currentPolicyProvider;

  @BeforeEach
  void init() {
    underTest = new ModelQuery(repository, currentPolicyProvider);
  }

  @Test
  void shouldGetModelDto() {
    // given
    OffsetDateTime beforePersist = now();
    persistModel();
    lenient().when(currentPolicyProvider.getCurrentPolicy())
        .thenReturn(of(CURRENT_POLICY_NAME));
    // when
    ModelDto result = underTest.get(MODEL_RESOURCE_NAME);
    // then
    assertThat(result.getId()).isEqualTo(MODEL_ID);
    assertThat(result.getName()).isEqualTo(MODEL_RESOURCE_NAME);
    assertThat(result.getPolicyName()).isEqualTo(POLICY_NAME);
    assertThat(result.getCreatedAt()).isAfter(beforePersist);
  }

  @Test
  void shouldGetDefaultModelDto() throws ModelMisconfiguredException {
    // given
    OffsetDateTime beforePersist = now();
    Mockito.when(currentPolicyProvider.getCurrentPolicy())
        .thenReturn(of(CURRENT_POLICY_NAME));
    // when
    ModelDto result = underTest.getDefault();
    // then
    assertThat(result.getId()).isEqualTo(DEFAULT_MODEL_ID);
    assertThat(result.getName()).isEqualTo(DEFAULT_MODEL_NAME);
    assertThat(result.getPolicyName()).isEqualTo(CURRENT_POLICY_NAME);
    assertThat(result.getCreatedAt()).isAfter(beforePersist);
  }

  private void persistModel() {
    repository.save(new Model(MODEL_ID, POLICY_NAME));
  }

  @Test
  void shouldThrowIfPolicyIsNotSet() {
    assertThatThrownBy(() -> underTest.getDefault())
        .isInstanceOf(ModelMisconfiguredException.class)
        .hasMessageContaining("policyName");
  }
}