package com.silenteight.adjudication.engine.comments.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;
import com.silenteight.adjudication.engine.common.protobuf.ObjectToMapConverter;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.util.Map;
import javax.annotation.Nonnull;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@RequiredArgsConstructor
@Slf4j
@Order(value = HIGHEST_PRECEDENCE)
@Component
class FreemarkerTemplateEngine implements TemplateEngineWithCache {

  private final Configuration freeMarker;

  @Override
  public boolean templateExists(String templateName) {
    try {
      return freeMarker.getTemplateLoader().findTemplateSource(getFullName(templateName)) != null;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public String generateComment(String templateName, AlertContext alertContext) {
    if (log.isDebugEnabled()) {
      log.debug("Generating comment (Freemarker): templateName={}, clientAlertIdentifier={}"
              + ", recommendedAction={}",
          templateName, alertContext.getAlertId(), alertContext.getRecommendedAction());
    }

    var templateContext = ObjectToMapConverter.convert(alertContext);

    return evaluateTemplate(templateName, templateContext);
  }

  @Override
  public String generateComment(String templateName, MatchContext matchContext) {
    if (log.isDebugEnabled()) {
      log.debug("Generating comment (Freemarker): templateName={}, matchId={}"
              + ", matchSolution={}",
          templateName, matchContext.getMatchId(), matchContext.getSolution());
    }

    var templateContext = ObjectToMapConverter.convert(matchContext);

    return evaluateTemplate(templateName, templateContext);
  }

  @Nonnull
  private String evaluateTemplate(String templateName, Map<String, Object> templateContext) {
    var compiledTemplate = getTemplate(templateName);
    var stringWriter = new StringWriter();

    try {
      compiledTemplate.process(templateContext, stringWriter);
    } catch (Exception e) {
      throw new CommentGenerationException(templateName, e);
    }

    return stringWriter.toString().trim();
  }

  private Template getTemplate(String templateName) {
    try {
      return freeMarker.getTemplate(getFullName(templateName));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public void invalidateCache() {
    log.debug("Freemarker comments cache will be invalidated ...");
    freeMarker.clearTemplateCache();
  }

  @Nonnull
  private static String getFullName(String templateName) {
    return templateName + ".ftl";
  }
}
