/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.comments.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LoadCommentTemplatesUseCaseTest {

  private LoadCommentTemplatesUseCase loadCommentTemplatesUseCase;
  private InMemoryCommentTemplateRepository inMemoryCommentTemplateRepository;

  @BeforeEach
  void setUp() {
    inMemoryCommentTemplateRepository = new InMemoryCommentTemplateRepository();
    var properties = new CommentProperties();
    loadCommentTemplatesUseCase =
        new LoadCommentTemplatesUseCase(properties, inMemoryCommentTemplateRepository);
  }

  @Test
  void shouldReadCommentTemplatesInFolder() {
    var commentTemplates = loadCommentTemplatesUseCase.loadCommentTemplates();
    assertThat("name-agent.ftl").isIn(commentTemplates);
    assertThat("alert-utils.ftl").isIn(commentTemplates);
  }

  @Test
  void shouldInsertCommentTemplate() {
    loadCommentTemplatesUseCase.loadCommentTemplates();
    var template =
        inMemoryCommentTemplateRepository.getCommentTemplateByTemplateName("name-agent.ftl");
    assertThat(template.getTemplate()).contains("name agent test example");
  }

  @Test
  void shouldDeletePreviousCommentTemplate() {
    inMemoryCommentTemplateRepository.save(
        CommentTemplate.builder().templateName("toBeDeleted").template("content").build());
    var commentTemplates = loadCommentTemplatesUseCase.loadCommentTemplates();
    assertThat("name-agent.ftl").isIn(commentTemplates);
    assertThat("alert-utils.ftl").isIn(commentTemplates);
  }
}
