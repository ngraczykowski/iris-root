package com.silenteight.adjudication.engine.solving.domain.comment;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CommentInputClientRepository {

  private final Map<Long, String> commentInputMap;
  private final ProtoMessageToObjectNodeConverter converter;

  public CommentInputClientRepository(
      final Map<Long, String> commentInputMap,
      final ProtoMessageToObjectNodeConverter converter) {
    this.commentInputMap = commentInputMap;
    this.converter = converter;
  }

  public void store(final long alert, final String commentInputPayload) {
    this.commentInputMap.put(alert, commentInputPayload);
  }

  public Map<String, Object> get(long alertId) {
    return Optional.ofNullable(this.commentInputMap.get(alertId))
        .flatMap(this.converter::convertJsonToMap)
        .orElseGet(() -> {
          log.warn("Returned empty comments inputs!");
          return Collections.emptyMap();
        });
  }
}
