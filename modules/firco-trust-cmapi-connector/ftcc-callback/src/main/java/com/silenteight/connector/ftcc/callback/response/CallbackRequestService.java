package com.silenteight.connector.ftcc.callback.response;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.common.database.partition.PartitionCreator;
import com.silenteight.connector.ftcc.common.dto.output.AckDto;
import com.silenteight.connector.ftcc.common.dto.output.ClientRequestDto;
import com.silenteight.connector.ftcc.common.resource.BatchResource;

import org.springframework.scheduling.annotation.Scheduled;

import java.time.Clock;
import java.time.OffsetDateTime;
import javax.annotation.PostConstruct;

import static com.silenteight.sep.base.common.support.jackson.JsonConversionHelper.INSTANCE;
import static java.time.OffsetDateTime.now;

@RequiredArgsConstructor
@Slf4j
class CallbackRequestService {

  private static final String TABLE_NAME = "ftcc_callback_request";

  @NonNull
  private final CallbackRequestRepository repository;
  @NonNull
  private final PartitionCreator partitionCreator;
  @NonNull
  private final Clock clock;

  @PostConstruct
  @Scheduled(cron = "@monthly")
  public void init() {
    createPartitions();
  }

  private void createPartitions() {
    OffsetDateTime now = now(clock);
    partitionCreator.createPartition(TABLE_NAME, now);
    partitionCreator.createPartition(TABLE_NAME, now.plusMonths(1));
  }

  void save(
      String batchName,
      ClientRequestDto clientRequestDto,
      String endpoint,
      AckDto ackDto,
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
      log.error("Could not store callback, batchName={}", batchName, e);
    }
  }
}
