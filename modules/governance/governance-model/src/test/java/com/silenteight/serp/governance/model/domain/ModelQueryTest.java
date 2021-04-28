package com.silenteight.serp.governance.model.domain;

import lombok.NonNull;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.serp.governance.model.ModelTestConfiguration;
import com.silenteight.serp.governance.model.domain.dto.ModelDto;
import com.silenteight.serp.governance.model.domain.exception.ModelMisconfiguredException;
import com.silenteight.serp.governance.model.domain.exception.TooManyModelsException;
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
import java.util.List;
import java.util.UUID;

import static com.silenteight.serp.governance.model.fixture.ModelFixtures.DEFAULT_MODEL_NAME;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.MODEL_ID;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.MODEL_RESOURCE_NAME;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.POLICY;
import static com.silenteight.serp.governance.policy.current.CurrentPolicyFixture.CURRENT_POLICY_NAME;
import static java.time.OffsetDateTime.now;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
@ContextConfiguration(classes = { ModelTestConfiguration.class })
@TestPropertySource("classpath:data-test.properties")
@AutoConfigureJsonTesters
class ModelQueryTest extends BaseDataJpaTest {

  @Qualifier("modelRepository")
  @Autowired
  ModelRepository repository;

  @Mock
  private CurrentPolicyProvider currentPolicyProvider;

  private ModelQuery underTest;

  @BeforeEach
  void init() {
    underTest = new ModelQuery(repository, currentPolicyProvider);
  }

  @Test
  void shouldGetModelByPolicy() {
    // given
    OffsetDateTime beforePersist = now();
    persistModel();

    // when
    List<ModelDto> result = underTest.getByPolicy(POLICY);

    // then
    assertThat(result).isNotEmpty();
    ModelDto model = result.get(0);
    assertThat(model.getName()).isEqualTo(MODEL_RESOURCE_NAME);
    assertThat(model.getPolicy()).isEqualTo(POLICY);
    assertThat(model.getCreatedAt()).isAfter(beforePersist);
  }

  @Test
  void shouldThrowExceptionWhenGetMultipeModelsForPolicy() {
    // given
    persistModel(randomUUID());
    persistModel(randomUUID());

    // when
    assertThatThrownBy(() -> underTest.getByPolicy(POLICY))
        .isInstanceOf(TooManyModelsException.class);
  }

  @Test
  void shouldGetDefaultModel() throws ModelMisconfiguredException {
    // given
    OffsetDateTime beforePersist = now();
    Mockito.when(currentPolicyProvider.getCurrentPolicy())
        .thenReturn(of(CURRENT_POLICY_NAME));

    // when
    ModelDto result = underTest.getDefault();

    // then
    assertThat(result.getName()).isEqualTo(DEFAULT_MODEL_NAME);
    assertThat(result.getPolicy()).isEqualTo(CURRENT_POLICY_NAME);
    assertThat(result.getCreatedAt()).isAfter(beforePersist);
  }

  @Test
  void shouldGetModel() {
    // given
    OffsetDateTime beforePersist = now();
    persistModel();
    lenient().when(currentPolicyProvider.getCurrentPolicy())
        .thenReturn(of(CURRENT_POLICY_NAME));

    // when
    ModelDto result = underTest.get(MODEL_RESOURCE_NAME);

    // then
    assertThat(result.getName()).isEqualTo(MODEL_RESOURCE_NAME);
    assertThat(result.getPolicy()).isEqualTo(POLICY);
    assertThat(result.getCreatedAt()).isAfter(beforePersist);
  }

  @Test
  void shouldThrowIfPolicyIsNotSet() {
    assertThatThrownBy(() -> underTest.getDefault())
        .isInstanceOf(ModelMisconfiguredException.class)
        .hasMessageContaining("policyName");
  }

  private void persistModel() {
    persistModel(MODEL_ID);
  }

  private void persistModel(@NonNull UUID modelId) {
    repository.save(new Model(modelId, POLICY));
  }
}
