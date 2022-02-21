package com.silenteight.adjudication.engine.analysis.commentinput.jdbc;

import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql
@ContextConfiguration(classes = RepositoryTestConfiguration.class)
class SelectCommentInputByAlertIdQueryIT extends BaseDataJpaTest {

  @Autowired
  protected JdbcTemplate jdbcTemplate;

  private SelectCommentInputByAlertIdQuery selectCommentInputByAlertIdQuery;

  @BeforeEach
  void setUp() {
    selectCommentInputByAlertIdQuery =
        new SelectCommentInputByAlertIdQuery(jdbcTemplate, new ObjectMapper());
  }

  @Test
  void shouldSelectCommentInput() {
    var commentInput = selectCommentInputByAlertIdQuery.execute(1);
    assertThat(commentInput).isPresent();
  }

  @Test
  void shouldNotSelectCommentInput() {
    var commentInput = selectCommentInputByAlertIdQuery.execute(10);
    assertThat(commentInput).isEmpty();
  }
}
