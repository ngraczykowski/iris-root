package com.silenteight.adjudication.engine.comments.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;
import com.silenteight.adjudication.engine.common.protobuf.ProtoStructConverter;

import com.mitchellbosecke.pebble.PebbleEngine;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

@RequiredArgsConstructor
@Service
@Slf4j
class GenerateCommentUseCase {

  private final ProtoStructConverter converter;
  private final PebbleEngine pebbleEngine;

  @Transactional(readOnly = true)
  String generateComment(String templateName, AlertContext alertContext) {
    var compiledTemplate = pebbleEngine.getTemplate(templateName);
    Writer writer = new StringWriter();
    try {
      compiledTemplate.evaluate(writer, converter.convert(alertContext));
    } catch (IOException e) {
      log.warn("Could not generate template", e);
      throw new CommentGenerationException(templateName);
    }
    return writer.toString();
  }

  @Scheduled(cron = "${ae.comments.template.cache.invalidation:0 */15 * * * ?}")
  void invalidateCache() {
    log.debug("Comments cache will be invalidated ...");
    pebbleEngine.getTemplateCache().invalidateAll();
  }
}
