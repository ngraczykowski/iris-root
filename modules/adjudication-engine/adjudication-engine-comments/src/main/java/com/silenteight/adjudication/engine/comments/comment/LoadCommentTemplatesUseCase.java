/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.comments.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serial;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Charsets.UTF_8;

@Service
@EnableConfigurationProperties(CommentProperties.class)
@RequiredArgsConstructor
@Slf4j
class LoadCommentTemplatesUseCase {

  private final CommentProperties properties;
  private final CommentTemplateRepository commentTemplateRepository;

  @EventListener(ApplicationReadyEvent.class)
  public List<String> loadCommentTemplates() {
    log.info("Deleting previous comment templates");
    commentTemplateRepository.deleteAll();
    var files = getFiles();
    log.info("Found {} comment templates for {} environment", files, properties.getEnvironment());
    var templates = files.stream().map(this::createCommentTemplate).collect(Collectors.toList());
    var savedTemplates = commentTemplateRepository.saveAll(templates);
    log.info("Saved {} comment templates", savedTemplates.size());
    return savedTemplates.stream()
        .map(CommentTemplate::getTemplateName)
        .collect(Collectors.toList());
  }

  CommentTemplate createCommentTemplate(String templateName) {
    var stream =
        LoadCommentTemplatesUseCase.class.getResourceAsStream(
            properties.getResourcePath() + properties.getEnvironment() + "/" + templateName);

    if (stream == null) {
      throw new CommentTemplateNotFoundException("Didn't find following template " + templateName);
    }

    String content;
    try {
      content = IOUtils.toString(stream, UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return CommentTemplate.builder().template(content).templateName(templateName).build();
  }

  List<String> getFiles() {
    var stream =
        LoadCommentTemplatesUseCase.class.getResourceAsStream(
            properties.getResourcePath() + properties.getEnvironment());

    if (stream == null) {
      log.error("Didn't find any files in given environment");
      return List.of();
    }

    return new BufferedReader(new InputStreamReader(stream)).lines().toList();
  }

  private static final class CommentTemplateNotFoundException extends RuntimeException {

    @Serial private static final long serialVersionUID = -2306754127917202565L;

    public CommentTemplateNotFoundException(String message) {
      super(message);
    }
  }
}
