package com.silenteight.adjudication.engine.analysis.commentinput.jdbc;

import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
@Sql
class JdbcCommentInputDataAccessIT extends BaseDataJpaTest {

  @Autowired
  protected JdbcTemplate jdbcTemplate;

  private SelectMissingAlertCommentInputQuery query;

  @BeforeEach
  public void setUp() {
    query = new SelectMissingAlertCommentInputQuery(jdbcTemplate, 10);
  }

  @Test
  void shouldSelectMissingCategories() {
    var missingCommentInputsResult = query.execute(1);

    assertThat(missingCommentInputsResult)
        .hasValueSatisfying(r -> assertThat(r.count()).isEqualTo(3));
  }
}
