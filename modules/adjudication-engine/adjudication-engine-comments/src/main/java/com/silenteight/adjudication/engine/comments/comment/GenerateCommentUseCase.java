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
    try {
      return registry.find(templateName).generateComment(templateName, alertContext);
    } catch (Exception e) {
      log.error(
          "Error occurred while generating comment: templateName={}, clientAlertIdentifier={}",
          templateName,
          alertContext.getAlertId(), e);
      //TODO(iwnek) the proper error state should be set in
      // com.silenteight.adjudication.api.v1.Recommendation message
      return "";
    }
  }
}
