package com.silenteight.adjudication.engine.comments.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
class GenerateCommentUseCase {

  private final TemplateEngineRegistry registry;

  @Transactional(readOnly = true)
  String generateComment(String templateName, AlertContext alertContext) {
    if (log.isDebugEnabled()) {
      log.debug(
          "Generating comment: templateName={}, clientAlertIdentifier={}, recommendedAction={}",
          templateName,
          alertContext.getAlertId(),
          alertContext.getRecommendedAction());
    }

    return registry.find(templateName).generateComment(templateName, alertContext);
  }
}
