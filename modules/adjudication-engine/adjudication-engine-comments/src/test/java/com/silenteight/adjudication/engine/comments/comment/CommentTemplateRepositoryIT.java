package com.silenteight.adjudication.engine.comments.comment;

import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
class CommentTemplateRepositoryIT extends BaseDataJpaTest {

  @Autowired
  private CommentTemplateRepository repository;

  @Test
  void shouldPersistDatasetAndFind() {
    String name = UUID.randomUUID().toString();
    var dataset = repository.save(CommentTemplateFixture
        .newCommentTemplateEntity(name, 0, "payload"));
    var datasetFound =
        repository.findFirstByTemplateNameOrderByRevisionDesc(name);

    assertThat(datasetFound.isPresent()).isTrue();
    assertThat(datasetFound.get())
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(dataset);
  }

  @Test
  void shouldFailOnSameRevisionInsert() {
    String name = UUID.randomUUID().toString();
    String payload = "{{name}} payload";

    repository.save(CommentTemplateFixture
        .newCommentTemplateEntity(name, 1, payload));
    assertThatThrownBy(() -> {
      repository.save(CommentTemplateFixture
          .newCommentTemplateEntity(name, 1, payload));
    });
  }

  @Test
  void shouldAddTemplatesWithDifferentValidFromDateAndGetTheLastOne() {
    String name = UUID.randomUUID().toString();
    String payload = "{{name}} payload-";
    CommentTemplate commentTemplateLatest = null;
    for (int i = 1; i <= 10; i++) {
      commentTemplateLatest = repository.save(CommentTemplateFixture
          .newCommentTemplateEntity(name, i, payload + i));
    }
    var datasetFound =
        repository.findFirstByTemplateNameOrderByRevisionDesc(name);

    assertThat(datasetFound.isPresent()).isTrue();

    assertThat(datasetFound.isPresent()).isTrue();
    assertThat(datasetFound.get())
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(commentTemplateLatest);
  }
}
