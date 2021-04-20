package com.silenteight.serp.governance.model.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.serp.governance.model.ModelTestConfiguration;
import com.silenteight.serp.governance.model.domain.dto.ModelDto;
import com.silenteight.serp.governance.model.domain.exception.ModelNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

import static com.silenteight.serp.governance.model.fixture.ModelFixtures.MODEL_ID;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.MODEL_RESOURCE_NAME;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.POLICY_NAME;
import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.Assertions.*;

@Transactional
@ContextConfiguration(classes = { ModelTestConfiguration.class })
@TestPropertySource("classpath:data-test.properties")
@AutoConfigureJsonTesters
class ModelQueryTest extends BaseDataJpaTest {

  @Autowired
  ModelQuery underTest;

  @Autowired
  ModelRepository repository;

  @Test
  void shouldGetModel() {
    // given
    OffsetDateTime beforePersist = now();
    persistModel();

    // when
    ModelDto result = underTest.get(MODEL_ID);

    // then
    assertThat(result.getId()).isEqualTo(MODEL_ID);
    assertThat(result.getName()).isEqualTo(MODEL_RESOURCE_NAME);
    assertThat(result.getCreatedAt()).isAfter(beforePersist);
  }

  @Test
  void shouldThrowExceptionWhenGetNotExistingModel() {
    assertThatThrownBy(() -> underTest.get(MODEL_ID))
        .isInstanceOf(ModelNotFoundException.class);
  }

  private void persistModel() {
    repository.save(new Model(MODEL_ID, POLICY_NAME));
  }
}
