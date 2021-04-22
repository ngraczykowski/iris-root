package com.silenteight.warehouse.report.storage;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;

import java.io.FileNotFoundException;

@AllArgsConstructor
public class ReportStorageService {

  private static final int UNKNOWN_FILE_SIZE = -1;
  private static final int PART_SIZE = 10485760;

  @NonNull
  private final MinioClient client;

  @NonNull
  private final String bucketName;

  void saveReport(Report report) {
    try {
      client.putObject(prepareReportObjectToSave(report));
    } catch (Exception e) {
      throw new AlertNotSavedException("Alert has not been successfully saved.", e);
    }
  }

  private PutObjectArgs prepareReportObjectToSave(Report report) throws FileNotFoundException {
    return PutObjectArgs.builder()
        .bucket(bucketName)
        .object(report.getReportName())
        .stream(
            report.getInputStream(), UNKNOWN_FILE_SIZE, PART_SIZE)
        .build();
  }
}
