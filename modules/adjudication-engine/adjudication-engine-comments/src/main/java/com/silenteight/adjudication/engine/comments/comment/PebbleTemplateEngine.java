package com.silenteight.adjudication.engine.comments.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;
import com.silenteight.adjudication.engine.common.protobuf.ObjectToMapConverter;

import com.mitchellbosecke.pebble.PebbleEngine;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Component
@Order
class PebbleTemplateEngine implements TemplateEngine {

  private final PebbleEngine pebbleEngine;

  @Override
  public boolean templateExists(String templateName) {
    return pebbleEngine.getLoader().resourceExists(templateName);
  }

  @Transactional(readOnly = true)
  public String generateComment(String templateName, AlertContext alertContext) {
    if (log.isDebugEnabled()) {
      log.debug("Generating comment (Pebble): templateName={}, clientAlertIdentifier={}"
              + ", recommendedAction={}",
          templateName, alertContext.getAlertId(), alertContext.getRecommendedAction());
    }

    var templateContext = ObjectToMapConverter.convert(alertContext);

    return evaluateTemplate(templateName, templateContext);
  }

  private String evaluateTemplate(String templateName, Map<String, Object> templateContext) {
    var compiledTemplate = pebbleEngine.getTemplate(templateName);
    var writer = new StringWriter();

    try {
      compiledTemplate.evaluate(writer, templateContext);
    } catch (IOException e) {
      throw new CommentGenerationException(templateName, e);
    }

    return writer.toString().trim();
  }

  @Scheduled(cron = "${ae.comments.template.cache.invalidation:0 * * * * ?}")
  void invalidateCache() {
    log.debug("Comments cache will be invalidated ...");
    pebbleEngine.getTemplateCache().invalidateAll();
  }
}
