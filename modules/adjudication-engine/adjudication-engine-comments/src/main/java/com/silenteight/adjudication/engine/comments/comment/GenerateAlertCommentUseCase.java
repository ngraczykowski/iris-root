package com.silenteight.adjudication.engine.comments.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
class GenerateAlertCommentUseCase {

  private final TemplateEngineRegistry registry;

  @Transactional(readOnly = true)
  String generateComment(
      String alertTemplateName, AlertContext alertContext) {
    if (log.isDebugEnabled()) {
      log.debug(
          "Generating comment: templateName={}, clientAlertIdentifier={}, recommendedAction={}",
          alertTemplateName,
          alertContext.getAlertId(),
          alertContext.getRecommendedAction());
    }
    try {
      return generateAlertComment(alertTemplateName, alertContext);
    } catch (Exception e) {
      log.error(
          "Error occurred while generating comment: templateName={}, clientAlertIdentifier={}",
          alertTemplateName,
          alertContext.getAlertId(), e);
      //TODO(iwnek) the proper error state should be set in
      // com.silenteight.adjudication.api.v1.Recommendation message
      return "";
    } finally {
      if (log.isTraceEnabled()) {
        log.trace("Alert context (ID = {}): {}",
            alertContext.getAlertId(), alertContext);
      }
    }
  }

  private String generateAlertComment(String templateName, AlertContext alertContext) {
    return registry.find(templateName).generateComment(templateName, alertContext);
  }
}
