package com.silenteight.serp.governance.model.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.serp.governance.model.ModelTestConfiguration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static com.silenteight.serp.governance.model.fixture.ModelFixtures.MODEL_ID;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.MAX_POLICY_NAME_LENGTH;
import static com.silenteight.serp.governance.policy.domain.SharedTestFixtures.POLICY_NAME_THAT_EXCEEDED_MAX_LENGTH;
import static com.silenteight.serp.governance.policy.domain.SharedTestFixtures.POLICY_NAME_WITH_MAX_LENGTH;
import static com.silenteight.serp.governance.policy.domain.SharedTestFixtures.USER;
import static java.lang.String.format;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@TestPropertySource("classpath:data-test.properties")
@ContextConfiguration(classes = { ModelTestConfiguration.class })
@AutoConfigureJsonTesters
class ModelServiceIT extends BaseDataJpaTest {

  private static final String POLICY_NAME_TOO_LONG_MSG =
      format("ERROR: value too long for type character varying(%s)", MAX_POLICY_NAME_LENGTH);

  @Autowired
  private ModelService underTest;

  @Autowired
  private ModelRepository modelRepository;

  @Test
  void createModel_succeeded_whenAllDataValid() {
    //when
    underTest.createModel(MODEL_ID, POLICY_NAME_WITH_MAX_LENGTH, USER);
    //then
    Optional<Model> model = modelRepository.findByModelId(MODEL_ID);
    assertThat(model).isPresent();
    assertThat(model.get().getModelId()).isEqualTo(MODEL_ID);
    assertThat(model.get().getPolicyName()).isEqualTo(POLICY_NAME_WITH_MAX_LENGTH);
  }

  @Test
  void createModel_throwsPersistenceException_whenPolicyNameToLong() {
    //when
    Throwable thrown = catchThrowable(() -> {
      underTest.createModel(MODEL_ID, POLICY_NAME_THAT_EXCEEDED_MAX_LENGTH, USER);
    });

    //then
    assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
    assertThat(thrown.getCause().getCause().getMessage()).isEqualTo(POLICY_NAME_TOO_LONG_MSG);
  }
}
