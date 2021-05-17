package com.silenteight.hsbc.bridge.bulk;

import lombok.*;

import com.silenteight.hsbc.bridge.bulk.exception.BulkWithGivenIdAlreadyCreatedException;

import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class StoreBulkUseCase {

  private final BulkIdRetriever bulkIdRetriever;
  private final BulkRepository bulkRepository;

  @Transactional
  public String handle(@NonNull StoreBulkUseCaseCommand command) {
    var bulkId = getBulkIdFromJson(command.getContent());
    validateBulkId(bulkId);

    createBulk(bulkId, command.isLearning(), command.getContent());

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

  private void createBulk(String bulkId, boolean learning, String json) {
    var bulk = new Bulk(bulkId, learning);
    var bulkPayload = new BulkPayloadEntity(json.getBytes(StandardCharsets.UTF_8));
    bulk.setPayload(bulkPayload);
    bulkRepository.save(bulk);
  }

  static class StoreBulkException extends RuntimeException {

    public StoreBulkException(String message) {
      super(message);
    }
  }

  @Builder
  @Value
  static class StoreBulkUseCaseCommand {

    String content;
    boolean learning;
  }
}
