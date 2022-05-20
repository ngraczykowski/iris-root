package com.silenteight.adjudication.engine.trace.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.trace.domain.Trace;
import com.silenteight.adjudication.engine.trace.domain.TraceRepository;
import com.silenteight.adjudication.engine.trace.listener.TraceMessage;
import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
@Slf4j
public class TraceProcess {

  private static final String RECOMMENDATION_PROPERTY_NAME = "recommendation";
  private static final ObjectMapper MAPPER = JsonConversionHelper.INSTANCE.objectMapper();
  private final TraceRepository traceRepository;

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  @Transactional
  public void handle(TraceMessage traceMessage) {
    String payload = traceMessage.getPayload();

    traceRepository.save(
        new Trace(traceMessage.getId(), traceMessage.getEventType(),
            payload, hashedRecommendation(payload),
            traceMessage.getOccurredOn().atOffset(ZoneOffset.UTC)));
  }

  private static String hashedRecommendation(String payload) {
    try {
      var o = MAPPER.readValue(payload, ObjectNode.class);
      var recommendation = o.findValue(RECOMMENDATION_PROPERTY_NAME);
      if (recommendation != null && !(recommendation instanceof NullNode)) {
        return sha512(recommendation.asText());
      }
    } catch (JsonProcessingException e) {
      log.trace("Processing payload", e);
    }
    return null;
  }

  private static String sha512(String payload) {
    return DigestUtils.sha512Hex(payload);
  }
}
