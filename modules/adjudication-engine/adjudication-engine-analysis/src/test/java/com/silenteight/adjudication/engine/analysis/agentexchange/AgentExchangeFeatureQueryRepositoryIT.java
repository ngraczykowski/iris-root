package com.silenteight.adjudication.engine.analysis.agentexchange;

import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
@Sql
class AgentExchangeFeatureQueryRepositoryIT extends BaseDataJpaTest {

  private static final UUID EXISTING_REQUEST_ID =
      UUID.fromString("980e1f4c-6c5b-45d2-8516-0998776a39c8");
  private static final UUID DUMMY_REQUEST_ID =
      UUID.fromString("00000000-1111-2222-3333-444444444444");

  @Autowired
  AgentExchangeFeatureQueryRepository repository;

  @Test
  void shouldReportExistence() {
    assertThat(repository.agentExchangeRequestExists(DUMMY_REQUEST_ID)).isFalse();
    assertThat(repository.agentExchangeRequestExists(EXISTING_REQUEST_ID)).isTrue();
  }

  @Test
  void shouldReturnFeatures() {
    var features = repository.findAllByRequestId(EXISTING_REQUEST_ID).collect(toList());

    assertThat(features)
        .hasSize(3)
        .anySatisfy(r -> assertThat(r.getFeature()).isEqualTo("features/test-1"))
        .anySatisfy(r -> assertThat(r.getFeature()).isEqualTo("features/test-2"))
        .anySatisfy(r -> assertThat(r.getFeature()).isEqualTo("features/test-3"))
    ;
  }
}
