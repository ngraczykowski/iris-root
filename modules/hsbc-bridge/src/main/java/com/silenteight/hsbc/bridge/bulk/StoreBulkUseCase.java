package com.silenteight.hsbc.bridge.bulk;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.exception.BulkWithGivenIdAlreadyCreatedException;

import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class StoreBulkUseCase {

  private final BulkIdRetriever bulkIdRetriever;
  private final BulkRepository bulkRepository;

  @Transactional
  public String handle(@NonNull String json) {
    var bulkId = getBulkIdFromJson(json);
    validateBulkId(bulkId);

    createBulk(bulkId, json);

    return bulkId;
  }

  private void validateBulkId(String bulkId) {
    if (bulkRepository.existsById(bulkId)) {
      throw new BulkWithGivenIdAlreadyCreatedException(bulkId);
    }
  }

  private String getBulkIdFromJson(String json) {
    return bulkIdRetriever.retrieve(json)
        .orElseThrow(() -> new StoreBulkException("Cannot locate the bulk Id!"));
  }

  private void createBulk(String bulkId, String json) {
    var bulk = new Bulk(bulkId);
    var bulkPayload = new BulkPayloadEntity(json.getBytes(StandardCharsets.UTF_8));
    bulk.setPayload(bulkPayload);
    bulkRepository.save(bulk);
  }

  class StoreBulkException extends RuntimeException {

    public StoreBulkException(String message) {
      super(message);
    }
  }
}
