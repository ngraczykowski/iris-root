package com.silenteight.adjudication.engine.comments.comment;

import com.silenteight.adjudication.engine.comments.comment.dto.CommentTemplateDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class CommentTemplateServiceTest {

  private static final String TEMPLATE_NAME = "templateName";
  private static final int REVISION = 1;
  private static final int INCREASED_REVISION = 2;
  private static final String TEMPLATE = "templateContent";
  private final InMemoryCommentTemplateRepository repo =
      new InMemoryCommentTemplateRepository();

  private CommentTemplateService testee;

  @BeforeEach
  void setUp() {
    testee = new CommentTemplateService(repo);
  }

  @Test
  void testAddTemplate() {
    // Given
    assertThat(repo.findFirstByTemplateNameOrderByRevisionDesc(TEMPLATE_NAME)).isNotPresent();

    // When
    testee.save(new CommentTemplateDto(TEMPLATE_NAME, REVISION, TEMPLATE));

    // Then
    var afterTemplate = repo.findFirstByTemplateNameOrderByRevisionDesc(TEMPLATE_NAME);
    assertTemplate(TEMPLATE_NAME, REVISION, TEMPLATE, afterTemplate);
  }

  @Test
  void testUpdateTemplate() {
    // Given
    testee.save(new CommentTemplateDto(TEMPLATE_NAME, REVISION, TEMPLATE));
    var beforeTemplate = repo.findFirstByTemplateNameOrderByRevisionDesc(TEMPLATE_NAME);
    assertTemplate(TEMPLATE_NAME, REVISION, TEMPLATE, beforeTemplate);

    // When
    testee.save(new CommentTemplateDto(TEMPLATE_NAME, INCREASED_REVISION, TEMPLATE));

    // Then
    var afterTemplate = repo.findFirstByTemplateNameOrderByRevisionDesc(TEMPLATE_NAME);
    assertTemplate(TEMPLATE_NAME, INCREASED_REVISION, TEMPLATE, afterTemplate);
  }

  @Test
  void testUpdateTemplateWithExistingRevision() {
    // Given
    CommentTemplateDto templateDto = new CommentTemplateDto(TEMPLATE_NAME, REVISION, TEMPLATE);
    testee.save(templateDto);
    var beforeTemplate = repo.findFirstByTemplateNameOrderByRevisionDesc(TEMPLATE_NAME);
    assertTemplate(TEMPLATE_NAME, REVISION, TEMPLATE, beforeTemplate);

    // When
    testee.save(templateDto);

    // Then
    var afterTemplate = repo.findFirstByTemplateNameOrderByRevisionDesc(TEMPLATE_NAME);
    assertTemplate(TEMPLATE_NAME, INCREASED_REVISION, TEMPLATE, afterTemplate);
  }

  private void assertTemplate(
      String templateName, int revision, String template,
      Optional<CommentTemplate> optionalTemplate) {

    assertThat(optionalTemplate).isPresent();
    assertThat(optionalTemplate.get())
        .isNotNull()
        .hasFieldOrPropertyWithValue("templateName", templateName)
        .hasFieldOrPropertyWithValue("revision", revision)
        .hasFieldOrPropertyWithValue("template", template);
  }
}
