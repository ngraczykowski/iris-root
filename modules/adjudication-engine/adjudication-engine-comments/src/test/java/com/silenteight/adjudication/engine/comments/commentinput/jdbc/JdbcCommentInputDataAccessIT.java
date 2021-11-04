package com.silenteight.adjudication.engine.comments.commentinput.jdbc;

import com.silenteight.adjudication.engine.comments.commentinput.domain.InsertCommentInputRequest;
import com.silenteight.adjudication.engine.testing.JdbcTestConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(classes = {
    JdbcTestConfiguration.class,
    JdbcAlertCommentInputDataAccess.class,
    JdbcAlertCommentInputConfiguration.class
})
@Sql
class JdbcCommentInputDataAccessIT extends BaseJdbcTest {

  @Autowired
  private JdbcAlertCommentInputDataAccess dataAccess;
  @Autowired
  private JdbcTemplate jdbcTemplate;
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Test
  void shouldInsertCommentInput() {
    dataAccess.insertCommentInput(InsertCommentInputRequest
        .builder()
        .alertId(1L)
        .value(OBJECT_MAPPER.createObjectNode())
        .build());
    assertThat(jdbcTemplate.queryForObject("SELECT count(*) FROM ae_alert_comment_input",
        Integer.class)).isEqualTo(1);
  }
}
