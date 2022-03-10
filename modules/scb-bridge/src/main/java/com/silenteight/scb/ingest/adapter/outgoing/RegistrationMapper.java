package com.silenteight.scb.ingest.adapter.outgoing;

import lombok.RequiredArgsConstructor;

import com.silenteight.registration.api.library.v1.RegisterBatchIn;
import com.silenteight.scb.ingest.domain.model.Batch;
import com.silenteight.scb.ingest.domain.payload.PayloadConverter;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RegistrationMapper {

  private final PayloadConverter converter;

  RegisterBatchIn toRegisterBatchIn(Batch batch) {
    return RegisterBatchIn.builder()
        .batchId(batch.id())
        .alertCount(batch.alertCount())
        .batchMetadata(converter.serializeFromObjectToJson(batch.metadata()))
        .build();
  }
}
