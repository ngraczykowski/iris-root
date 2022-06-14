package com.silenteight.adjudication.engine.comments.comment;

import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import com.google.common.io.CharStreams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.UUID;

import static com.silenteight.adjudication.engine.comments.comment.CommentTemplateFixture.commentTemplate;
import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
class PebbleCommentTemplateLoaderIT extends BaseDataJpaTest {

  protected static final String PAYLOAD = "payload";
  @Autowired
  private CommentTemplateRepository repository;

  private PebbleCommentTemplateLoader loader;

  @BeforeEach
  void setUp() {
    loader = new PebbleCommentTemplateLoader(repository);
  }

  @Test
  void shouldLoadSavedTemplate() throws IOException {
    String name = UUID.randomUUID().toString();

    var payload = PAYLOAD;
    repository.save(pebbleTemplate(name, payload));

    assertThat(readTemplate(name)).isEqualTo(payload);
  }

  @Test
  void shouldUsePrefix() throws IOException {
    repository.save(pebbleTemplate("prefix/name", PAYLOAD));

    loader.setPrefix("prefix/");
    assertThat(readTemplate("name")).isEqualTo(PAYLOAD);

    loader.setPrefix("prefix");
    assertThat(readTemplate("name")).isEqualTo(PAYLOAD);
  }

  @Test
  void shouldUseSuffix() throws IOException {
    repository.save(pebbleTemplate("name", PAYLOAD));

    assertThat(readTemplate("name")).isEqualTo(PAYLOAD);
  }

  private String readTemplate(String name) throws IOException {
    try (var reader = loader.getReader(name)) {
      return CharStreams.toString(reader);
    }
  }

  private CommentTemplate pebbleTemplate(String name, String payload) {
    return commentTemplate(name + ".peb", payload);
  }
}
