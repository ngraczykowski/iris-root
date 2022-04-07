package com.silenteight.connector.ftcc.callback.response;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.common.dto.output.AckDto;
import com.silenteight.connector.ftcc.common.dto.output.ClientRequestDto;
import com.silenteight.connector.ftcc.common.resource.BatchResource;

import static com.silenteight.sep.base.common.support.jackson.JsonConversionHelper.INSTANCE;

@RequiredArgsConstructor
@Slf4j
class CallbackRequestService {

  @NonNull
  private final CallbackRequestRepository repository;

  void save(
      String batchName, ClientRequestDto clientRequestDto, String endpoint, AckDto ackDto,
      Integer code) {
    try {
      var payload = INSTANCE.serializeObject(clientRequestDto);
      var response = INSTANCE.serializeObject(ackDto);

      repository.save(CallbackRequestEntity.builder()
          .batchId(BatchResource.fromResourceName(batchName))
          .payload(payload)
          .endpoint(endpoint)
          .response(response)
          .code(code)
          .build());
    } catch (Exception e) {
      log.error("Can not store callback, batchName={}", batchName, e);
    }
  }
}
