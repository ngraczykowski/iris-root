package com.silenteight.universaldatasource.app.commentinput.adapter.outgoing.jdbc;

import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.silenteight.universaldatasource.common.resource.AlertName.getListOfAlerts;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql({ "populate_comment_inputs.sql" })
@Import({
    JdbcCommentInputDataAccess.class,
    JdbcCommentInputConfiguration.class,
    StreamCommentInputQuery.class,
    SelectCommentInputsQuery.class,
    DeleteCommentInputsQuery.class
})
class CommentInputDataAccessIT extends BaseJdbcTest {

  @Autowired
  private JdbcCommentInputDataAccess commentInputDataAccess;

  @Test
  void shouldDeleteCommentInputs() {

    var alerts = getListOfAlerts(List.of(1, 2, 3, 4, 5));

    assertEquals(5, commentInputDataAccess.delete(alerts));
    assertEquals(5, jdbcTemplate.queryForObject(
        "SELECT count(*) FROM uds_comment_input", Integer.class));
  }
}
