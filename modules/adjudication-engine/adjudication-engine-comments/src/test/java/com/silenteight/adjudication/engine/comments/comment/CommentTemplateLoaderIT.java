package com.silenteight.adjudication.engine.comments.comment;

import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import com.google.common.io.CharStreams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.UUID;

import static com.silenteight.adjudication.engine.comments.comment.CommentTemplateFixture.commentTemplate;
import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
class CommentTemplateLoaderIT extends BaseDataJpaTest {

  protected static final String PAYLOAD = "payload";
  @Autowired
  private CommentTemplateRepository repository;

  private CommentTemplateLoader loader;

  @BeforeEach
  void setUp() {
    loader = new CommentTemplateLoader(repository);
  }

  @Test
  void shouldLoadSavedTemplate() throws IOException {
    String name = UUID.randomUUID().toString();

    repository.save(commentTemplate(name, PAYLOAD));

    assertThat(readTemplate(name)).isEqualTo(PAYLOAD);
  }

  @Test
  void shouldFailOnSameRevisionInsert() {
    var name = UUID.randomUUID().toString();
    var payload = "{{name}} payload";
    var template = commentTemplate(name, 10, payload);

    repository.save(template);

    var sameTemplate = commentTemplate(name, 10, payload);
    assertThatThrownBy(() -> repository.save(sameTemplate))
        .isInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  void shouldReadLatestVersionOfTheTemplate() throws IOException {
    String name = UUID.randomUUID().toString();

    repository.save(commentTemplate(name, "payload ver 1"));
    repository.save(commentTemplate(name, "payload ver 2"));

    assertThat(readTemplate(name)).isEqualTo("payload ver 2");

    repository.save(commentTemplate(name, "payload ver 3"));

    assertThat(readTemplate(name)).isEqualTo("payload ver 3");
  }

  @Test
  void shouldUsePrefix() throws IOException {
    repository.save(commentTemplate("prefix/name", PAYLOAD));

    loader.setPrefix("prefix/");
    assertThat(readTemplate("name")).isEqualTo(PAYLOAD);

    loader.setPrefix("prefix");
    assertThat(readTemplate("name")).isEqualTo(PAYLOAD);
  }

  @Test
  void shouldUseSuffix() throws IOException {
    repository.save(commentTemplate("name.peb", PAYLOAD));

    loader.setSuffix(".peb");
    assertThat(readTemplate("name")).isEqualTo(PAYLOAD);
  }

  private String readTemplate(String name) throws IOException {
    try (var reader = loader.getReader(name)) {
      return CharStreams.toString(reader);
    }
  }
}
