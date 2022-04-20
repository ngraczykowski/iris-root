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
    Optional<CommentTemplate> beforeTemplate =
        repo.findFirstByTemplateNameOrderByRevisionDesc(TEMPLATE_NAME);
    assertThat(beforeTemplate).isNotPresent();

    // When
    CommentTemplateDto templateDto = new CommentTemplateDto(TEMPLATE_NAME, REVISION, TEMPLATE);
    testee.save(templateDto);

    // Then
    CommentTemplate afterTemplate =
        repo.findFirstByTemplateNameOrderByRevisionDesc(TEMPLATE_NAME).get();
    assertThat(afterTemplate.getTemplateName()).isEqualTo(TEMPLATE_NAME);
    assertThat(afterTemplate.getRevision()).isEqualTo(REVISION);
    assertThat(afterTemplate.getTemplate()).isEqualTo(TEMPLATE);
  }

  @Test
  void testUpdateTemplate() {
    // Given
    CommentTemplateDto templateDto = new CommentTemplateDto(TEMPLATE_NAME, REVISION, TEMPLATE);
    testee.save(templateDto);
    Optional<CommentTemplate> beforeTemplate =
        repo.findFirstByTemplateNameOrderByRevisionDesc(TEMPLATE_NAME);
    assertThat(beforeTemplate).isPresent();

    // When
    CommentTemplateDto newTemplateDto =
        new CommentTemplateDto(TEMPLATE_NAME, INCREASED_REVISION, TEMPLATE);
    testee.save(newTemplateDto);

    // Then
    CommentTemplate afterTemplate =
        repo.findFirstByTemplateNameOrderByRevisionDesc(TEMPLATE_NAME).get();
    assertThat(afterTemplate.getTemplateName()).isEqualTo(TEMPLATE_NAME);
    assertThat(afterTemplate.getRevision()).isEqualTo(INCREASED_REVISION);
    assertThat(afterTemplate.getTemplate()).isEqualTo(TEMPLATE);
  }

  @Test
  void testUpdateTemplateWithExistingRevision() {
    // Given
    CommentTemplateDto templateDto = new CommentTemplateDto(TEMPLATE_NAME, REVISION, TEMPLATE);
    testee.save(templateDto);
    Optional<CommentTemplate> beforeTemplate =
        repo.findFirstByTemplateNameOrderByRevisionDesc(TEMPLATE_NAME);
    assertThat(beforeTemplate).isPresent();

    // When
    testee.save(templateDto);

    // Then
    CommentTemplate afterTemplate =
        repo.findFirstByTemplateNameOrderByRevisionDesc(TEMPLATE_NAME).get();
    assertThat(afterTemplate.getTemplateName()).isEqualTo(TEMPLATE_NAME);
    assertThat(afterTemplate.getRevision()).isEqualTo(INCREASED_REVISION);
    assertThat(afterTemplate.getTemplate()).isEqualTo(TEMPLATE);
  }
}
