/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.builder.consumerrepo;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.HitDetails;

import java.util.Optional;
import java.util.function.BiConsumer;

public class HitDetailsAttributeConsumerRepository
    implements ConsumerRepository<HitDetails, String> {

  private final MappingConsumerRepository<HitDetails, String> mappingRepository =
      new MappingConsumerRepository<>();

  public HitDetailsAttributeConsumerRepository() {
    mappingRepository.register("SystemId", HitDetails::setSystemId);
    mappingRepository.register("Limited", Integer::parseInt, HitDetails::setLimited);
    mappingRepository.register("HasSndRcvIn", v -> Boolean.TRUE, HitDetails::setHasSndRcvIn);
  }

  @Override
  public Optional<BiConsumer<HitDetails, String>> find(String key) {
    return mappingRepository.find(key);
  }
}
