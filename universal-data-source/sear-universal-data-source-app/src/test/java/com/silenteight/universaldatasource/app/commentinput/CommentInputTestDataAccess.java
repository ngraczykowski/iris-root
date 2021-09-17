package com.silenteight.universaldatasource.app.commentinput;

import org.springframework.jdbc.core.JdbcTemplate;

class CommentInputTestDataAccess {

  static int generatedCommentInputsCount(JdbcTemplate jdbcTemplate) {
    return jdbcTemplate.queryForObject(
        "SELECT COUNT(*)\n"
            + "FROM uds_comment_input;", Integer.class);
  }
}
