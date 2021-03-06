/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.comments.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.google.common.io.Files;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
  private final ResourceLoader resourceLoader;

  @EventListener(ApplicationReadyEvent.class)
  public List<String> loadCommentTemplates() {
    log.info("Deleting previous comment templates");
    commentTemplateRepository.deleteAll();
    var files = getFiles();
    log.info(
        "Found {} comment templates for {} environment", files, properties.getEnvironment());
    var templates = files.stream().map(this::createCommentTemplate).collect(Collectors.toList());
    var savedTemplates = commentTemplateRepository.saveAll(templates);
    log.info("Saved {} comment templates", savedTemplates.size());
    return savedTemplates.stream()
        .map(CommentTemplate::getTemplateName)
        .collect(Collectors.toList());
  }

  CommentTemplate createCommentTemplate(String templateName) {
    var resource =
        resourceLoader.getResource(
            properties.getResourcePath() + properties.getEnvironment() + "/" + templateName);
    String content;
    try {
      var templateFile = resource.getFile();
      content = Files.asCharSource(templateFile, UTF_8).read();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return CommentTemplate.builder().template(content).templateName(templateName).build();
  }

  List<String> getFiles() {
    var resource =
        resourceLoader.getResource(properties.getResourcePath() + properties.getEnvironment());
    try {
      return new BufferedReader(new InputStreamReader(resource.getInputStream())).lines().toList();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
