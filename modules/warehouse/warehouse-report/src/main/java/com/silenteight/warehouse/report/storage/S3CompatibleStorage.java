package com.silenteight.warehouse.report.storage;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.reporting.Report;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
class S3CompatibleStorage implements ReportStorage {

  @NonNull
  private final S3Client s3Client;

  @NonNull
  private final ReportStorageRequestStatusCheck reportStorageRequestStatusCheck;

  @NonNull
  private final String bucketName;

  @Override
  public void saveReport(Report report) {
    var putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(report.getReportName())
        .build();
    var requestBody = RequestBody.fromFile(report.getReportPath());

    s3Client.putObject(putObjectRequest, requestBody);
    reportStorageRequestStatusCheck.checkPersistenceStatus(report, bucketName);
  }

  @Override
  public void removeReport(String reportName) {
    var deleteObjectRequest = DeleteObjectRequest.builder()
        .bucket(bucketName)
        .key(reportName)
        .build();

    s3Client.deleteObject(deleteObjectRequest);
  }

  @Override
  public FileDto getReport(String reportName) {
    var getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(reportName)
        .build();

    var inputStream = s3Client.getObject(getObjectRequest);

    return FileDto.builder()
        .name(reportName)
        .content(inputStream)
        .build();
  }

  @Override
  public void removeReports(Collection<String> reportNames) {
    var toDelete = reportNames.stream()
        .map(name -> ObjectIdentifier.builder().key(name).build())
        .collect(Collectors.toList());

    var deleteObjectRequest = DeleteObjectsRequest.builder()
        .bucket(bucketName)
        .delete(Delete.builder().objects(toDelete).build())
        .build();

    s3Client.deleteObjects(deleteObjectRequest);
  }
}
