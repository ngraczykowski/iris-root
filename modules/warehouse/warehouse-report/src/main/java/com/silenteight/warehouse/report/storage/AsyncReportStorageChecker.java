package com.silenteight.warehouse.report.storage;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.persistence.ReportPersistenceService;

import software.amazon.awssdk.core.retry.backoff.FixedDelayBackoffStrategy;
import software.amazon.awssdk.core.waiters.WaiterOverrideConfiguration;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@RequiredArgsConstructor
@Slf4j
public class AsyncReportStorageChecker {

  private static final Duration FIXED_BACKOFF = Duration.ofSeconds(3);

  @NonNull
  private final S3Client s3Client;
  @NonNull
  private final ExecutorService executorService;
  @NonNull
  private final ReportPersistenceService reportPersistenceService;

  public void runStatusCheck(@NonNull String key, @NonNull String bucket, @NonNull Long id) {
    var headObjectRequest = HeadObjectRequest.builder()
        .key(key)
        .bucket(bucket)
        .build();

    CompletableFuture.supplyAsync(() -> runStatusCheck(headObjectRequest), executorService)
        .thenAccept(resultHolder -> updateStatus(resultHolder, id, key));
  }

  private ResultHolder runStatusCheck(HeadObjectRequest request) {
    var resultHolder = new ResultHolder();

    WaiterResponse<HeadObjectResponse> response = s3Client.waiter()
        .waitUntilObjectExists(request, waiterStrategy());

    response.matched().response().ifPresent(done -> resultHolder.setFullPersisted(true));
    response.matched().exception().ifPresent(fail -> resultHolder.setFullPersisted(false));

    return resultHolder;
  }

  private static WaiterOverrideConfiguration waiterStrategy() {
    return WaiterOverrideConfiguration.builder()
        .backoffStrategy(FixedDelayBackoffStrategy.create(FIXED_BACKOFF))
        .build();
  }

  private void updateStatus(
      @NonNull ResultHolder resultHolder, @NonNull Long id, @NonNull String key) {
    if (resultHolder.isFullPersisted) {
      reportPersistenceService.generationSuccessful(id);
      log.info(
          "Report has been successfully saved in S3-compatible storage, reportName={}", key);
    } else {
      reportPersistenceService.generationFail(id);
      log.error(
          "Report has not been successfully saved in S3-compatible storage, reportName={}", key);
    }
  }

  @Data
  private static class ResultHolder {

    private boolean isFullPersisted;
  }
}
