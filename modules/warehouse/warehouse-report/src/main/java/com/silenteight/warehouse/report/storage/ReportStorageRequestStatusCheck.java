package com.silenteight.warehouse.report.storage;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.persistence.ReportPersistenceService;
import com.silenteight.warehouse.report.reporting.Report;

import software.amazon.awssdk.core.retry.backoff.FixedDelayBackoffStrategy;
import software.amazon.awssdk.core.waiters.WaiterOverrideConfiguration;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

@RequiredArgsConstructor
@Slf4j
public class ReportStorageRequestStatusCheck {

  private static final Duration FIXED_BACKOFF = Duration.ofSeconds(3);

  @NonNull
  private final S3Client s3Client;
  @NonNull
  private final ReportPersistenceService reportPersistenceService;

  @SneakyThrows
  public void checkPersistenceStatus(@NonNull Report report, @NonNull String bucket) {
    var request = HeadObjectRequest.builder()
        .key(report.getReportName())
        .bucket(bucket)
        .build();

    var result = checkPersistenceStatus(request);
    updateReportData(report, result);
  }

  private ResultHolder checkPersistenceStatus(HeadObjectRequest request) {
    var resultHolder = new ResultHolder();

    WaiterResponse<HeadObjectResponse> response = s3Client.waiter()
        .waitUntilObjectExists(request, waiterStrategy());

    response.matched().response().ifPresent(done -> resultHolder.setFullPersisted(true));
    response.matched().exception().ifPresent(fail -> resultHolder.setFullPersisted(false));

    return resultHolder;
  }

  private void updateReportData(Report report, ResultHolder result) {
    if (result.isFullPersisted) {
      reportPersistenceService.generationSuccessful(report.getReportId());
      deleteTempFile(report.getReportPath());
      log.info(
          "Report has been successfully saved in S3-compatible storage, reportName={}",
          report.getReportName());
    } else {
      reportPersistenceService.generationFail(report.getReportId());
      log.error(
          "Report has not been successfully saved in S3-compatible storage, reportName={}",
          report.getReportName());
    }
  }

  private static WaiterOverrideConfiguration waiterStrategy() {
    return WaiterOverrideConfiguration.builder()
        .backoffStrategy(FixedDelayBackoffStrategy.create(FIXED_BACKOFF))
        .build();
  }

  private static void deleteTempFile(Path path) {
    try {
      boolean deleted = Files.deleteIfExists(path);
      log.debug("File {} deleted={}", path, deleted);
    } catch (IOException e) {
      log.error("Error while deleting temp file: {}", path);
    }
  }

  @Data
  private static class ResultHolder {

    private boolean isFullPersisted;
  }
}
