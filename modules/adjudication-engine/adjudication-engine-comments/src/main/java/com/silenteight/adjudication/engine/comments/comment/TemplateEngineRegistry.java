package com.silenteight.adjudication.engine.comments.comment;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@RequiredArgsConstructor
@Service
class TemplateEngineRegistry {

  private final List<TemplateEngine> templateEngines;

  TemplateEngine find(String templateName) {
    return templateEngines
        .stream()
        .filter(r -> r.templateExists(templateName)).findFirst()
        .orElseThrow(() -> new TemplateNotFoundException(templateName));
  }

  static class TemplateNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 8135827362705696588L;

    TemplateNotFoundException(String templateName) {
      super(format("Template not found: %s. ", templateName));
    }
  }
}
