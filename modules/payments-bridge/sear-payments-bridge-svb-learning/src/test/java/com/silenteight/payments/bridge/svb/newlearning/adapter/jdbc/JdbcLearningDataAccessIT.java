package com.silenteight.payments.bridge.svb.newlearning.adapter.jdbc;

import com.silenteight.payments.bridge.svb.newlearning.domain.ObjectPath;
import com.silenteight.payments.bridge.testing.JdbcTestConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql
@ContextConfiguration(classes = {
    JdbcTestConfiguration.class,
    JdbcLearningDataAccess.class,
    InsertNonExistingFileNamesQuery.class
})
class JdbcLearningDataAccessIT extends BaseJdbcTest {

  @Autowired
  private JdbcLearningDataAccess dataAccess;
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void shouldInsertOneName() {
    var savedNames = dataAccess.saveNonProcessedFileNames(
        List.of(ObjectPath.builder().name("test.csv").bucket("bucket").build()));
    var savedCount =
        jdbcTemplate.queryForObject("SELECT count(*) FROM pb_learning_file", Integer.class);

    assertThat(savedCount).isEqualTo(2);
    assertThat(savedNames.size()).isEqualTo(1);
  }

  @Test
  void shouldInsertTwoNamesAndOneNot() {
    var existing = ObjectPath.builder().name("existingName").bucket("bucket").build();
    var newFile = ObjectPath.builder().name("newFile").bucket("bucket").build();
    var newFile2 = ObjectPath.builder().name("newFile2").bucket("bucket").build();

    var savedNames = dataAccess.saveNonProcessedFileNames(List.of(existing, newFile, newFile2));
    var savedCount =
        jdbcTemplate.queryForObject("SELECT count(*) FROM pb_learning_file", Integer.class);

    assertThat(savedCount).isEqualTo(3);
    assertThat(savedNames.size()).isEqualTo(2);
  }
}
