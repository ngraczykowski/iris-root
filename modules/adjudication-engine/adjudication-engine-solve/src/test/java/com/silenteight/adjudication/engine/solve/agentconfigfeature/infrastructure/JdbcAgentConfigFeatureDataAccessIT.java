package com.silenteight.adjudication.engine.solve.agentconfigfeature.infrastructure;

import com.silenteight.adjudication.engine.solve.agentconfigfeature.dto.AgentConfigFeatureName;
import com.silenteight.adjudication.engine.testing.JdbcTestConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import javax.annotation.Nullable;

import static com.silenteight.adjudication.engine.solve.agentconfigfeature.infrastructure.Fixtures.dateAgentFeature;
import static com.silenteight.adjudication.engine.solve.agentconfigfeature.infrastructure.Fixtures.nameAgentFeature;
import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = {
    JdbcTestConfiguration.class,
    AgentConfigFeatureDataAccessConfiguration.class,
    JdbcAgentConfigFeatureDataAccess.class,
})
class JdbcAgentConfigFeatureDataAccessIT extends BaseJdbcTest {

  @Autowired
  private JdbcAgentConfigFeatureDataAccess dataAccess;

  @Test
  void insertsNewFeatures() {
    var affected = addFeatures(nameAgentFeature("name"), dateAgentFeature("dob"));
    var features = countFeatures();

    assertThat(features).isEqualTo(affected).isEqualTo(2);
  }

  @Test
  void doesNotInsertDuplicates() {
    addFeatures(nameAgentFeature("name"), dateAgentFeature("dob"));
    var affected = addFeatures(dateAgentFeature("dob"));

    assertThat(affected).isZero();
  }

  @Nullable
  private Integer countFeatures() {
    return jdbcTemplate.queryForObject(
        "SELECT count(1) FROM ae_agent_config_feature", Integer.class);
  }

  private int addFeatures(AgentConfigFeatureName... features) {
    return dataAccess.addFeatures(List.of(features));
  }
}
