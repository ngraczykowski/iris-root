package com.silenteight.adjudication.engine.comments.comment;

import com.silenteight.adjudication.engine.comments.comment.dto.CommentTemplateDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class CommentTemplateServiceTest {

  private static final String TEMPLATE_NAME = "templateName";
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
    assertThat(repo.findFirstByTemplateName(TEMPLATE_NAME)).isNotPresent();

    // When
    testee.save(new CommentTemplateDto(TEMPLATE_NAME, TEMPLATE));

    // Then
    var afterTemplate = repo.findFirstByTemplateName(TEMPLATE_NAME);
    assertTemplate(TEMPLATE_NAME, TEMPLATE, afterTemplate);
  }

  @Test
  void testUpdateTemplate() {
    // Given
    testee.save(new CommentTemplateDto(TEMPLATE_NAME, TEMPLATE));
    var beforeTemplate = repo.findFirstByTemplateName(TEMPLATE_NAME);
    assertTemplate(TEMPLATE_NAME, TEMPLATE, beforeTemplate);

    // When
    testee.save(new CommentTemplateDto(TEMPLATE_NAME, TEMPLATE));

    // Then
    var afterTemplate = repo.findFirstByTemplateName(TEMPLATE_NAME);
    assertTemplate(TEMPLATE_NAME, TEMPLATE, afterTemplate);
  }

  @Test
  void testUpdateTemplateWithExistingRevision() {
    // Given
    CommentTemplateDto templateDto = new CommentTemplateDto(TEMPLATE_NAME, TEMPLATE);
    testee.save(templateDto);
    var beforeTemplate = repo.findFirstByTemplateName(TEMPLATE_NAME);
    assertTemplate(TEMPLATE_NAME, TEMPLATE, beforeTemplate);

    // When
    testee.save(templateDto);

    // Then
    var afterTemplate = repo.findFirstByTemplateName(TEMPLATE_NAME);
    assertTemplate(TEMPLATE_NAME, TEMPLATE, afterTemplate);
  }

  private void assertTemplate(
      String templateName, String template,
      Optional<CommentTemplate> optionalTemplate) {

    assertThat(optionalTemplate).isPresent();
    assertThat(optionalTemplate.get())
        .isNotNull()
        .hasFieldOrPropertyWithValue("templateName", templateName)
        .hasFieldOrPropertyWithValue("template", template);
  }
}
