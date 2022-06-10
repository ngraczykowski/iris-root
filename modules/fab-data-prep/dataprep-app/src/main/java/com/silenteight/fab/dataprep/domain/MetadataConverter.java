package com.silenteight.fab.dataprep.domain;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.model.Metadata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Splitter;

import static com.google.common.collect.Streams.findLast;
import static com.silenteight.sep.base.common.support.jackson.JsonConversionHelper.INSTANCE;

@Slf4j
@UtilityClass
class MetadataConverter {

  static String getMetadata(String systemId) {
    var metadata = createMetadata(systemId);
    try {
      return INSTANCE.objectMapper().writeValueAsString(metadata);
    } catch (JsonProcessingException e) {
      log.warn("Unable to create metadata", e);
      return "{}";
    }
  }

  private static Metadata createMetadata(String systemId) {
    return Metadata.builder()
        .systemId(systemId)
        .recordId(getRecordId(systemId))
        .build();
  }

  private static String getRecordId(String systemId) {
    return findLast(Splitter.on('!').splitToStream(systemId))
        .orElse("");
  }
}
