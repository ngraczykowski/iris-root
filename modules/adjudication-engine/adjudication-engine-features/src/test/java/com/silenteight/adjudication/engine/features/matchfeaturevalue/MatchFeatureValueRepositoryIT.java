package com.silenteight.adjudication.engine.features.matchfeaturevalue;

import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
@Sql
class MatchFeatureValueRepositoryIT extends BaseDataJpaTest {

  private static final String MATCH = "MATCH";
  private static final String NO_MATCH = "NO_MATCH";
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  protected static final String REASON_FIELD = "testReason";

  @Autowired
  private MatchFeatureValueRepository repository;

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void savesMatchFeatureValues() {
    assertThatNoException().isThrownBy(() -> {
      repository.saveAll(List.of(
          matchFeatureValue(1, 1, MATCH),
          matchFeatureValue(1, 2, NO_MATCH),
          matchFeatureValue(2, 1, MATCH),
          matchFeatureValue(2, 2, NO_MATCH),
          matchFeatureValue(3, 1, MATCH),
          matchFeatureValue(3, 2, NO_MATCH),
          matchFeatureValue(4, 1, NO_MATCH),
          matchFeatureValue(4, 2, MATCH)
      ));
      entityManager.flush();
    });

    var count =
        jdbcTemplate.queryForObject("SELECT count(*) FROM ae_match_feature_value", Long.class);

    assertThat(count).isEqualTo(8);
  }

  @Test
  void reasonDeserializesCorrectly() {
    var savedEntity = repository.save(matchFeatureValue(1, 1, MATCH));

    entityManager.flush();
    entityManager.clear();

    var actual = entityManager.find(MatchFeatureValue.class, new MatchFeatureValueKey(1L, 1L));

    assertThat(actual).usingRecursiveComparison().isEqualTo(savedEntity);
    assertThat(actual.getReason().requiredAt("/" + REASON_FIELD).asText())
        .startsWith("the value was ");
  }

  private MatchFeatureValue matchFeatureValue(
      long matchId, long agentConfigFeatureId, String value) {

    var reason = OBJECT_MAPPER.createObjectNode();

    reason.put(REASON_FIELD, "the value was " + value);

    return MatchFeatureValue.builder()
        .id(new MatchFeatureValueKey(matchId, agentConfigFeatureId))
        .value(value)
        .reason(reason)
        .build();
  }
}
