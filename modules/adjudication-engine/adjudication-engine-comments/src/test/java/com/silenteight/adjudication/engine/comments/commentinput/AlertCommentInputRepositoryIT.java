package com.silenteight.adjudication.engine.comments.commentinput;

import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
@Sql
class AlertCommentInputRepositoryIT extends BaseDataJpaTest {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Autowired
  private AlertCommentInputRepository repository;

  @Test
  void shouldStoreAlertCommentInput() {
    var alertId = 1L;

    var dataset = repository.save(
        CommentInputFixture.newAlertInputFixture(alertId, OBJECT_MAPPER.createObjectNode())
    );

    var alertCommentInput =
        repository.findAllByAlertId(alertId);

    assertThat(alertCommentInput).isPresent();
    assertThat(alertCommentInput.get())
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(dataset);
  }

}
