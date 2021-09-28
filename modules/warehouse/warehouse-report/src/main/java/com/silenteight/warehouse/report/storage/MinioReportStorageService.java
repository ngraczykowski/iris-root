package com.silenteight.warehouse.report.storage;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.filestorage.api.FileRemover;
import com.silenteight.sep.filestorage.api.FileRetriever;
import com.silenteight.sep.filestorage.api.FileUploader;
import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.sep.filestorage.api.dto.StoreFileRequestDto;
import com.silenteight.warehouse.report.reporting.Report;

import java.io.FileNotFoundException;
import java.util.List;

@Slf4j
@AllArgsConstructor
class MinioReportStorageService implements ReportStorage {

  private static final int UNKNOWN_FILE_SIZE = -1;

  @NonNull
  private final FileUploader fileUploader;

  @NonNull
  private final FileRemover fileRemover;

  @NonNull
  private final FileRetriever fileRetriever;

  @NonNull
  private final String bucketName;

  @Override
  public void saveReport(Report report) {
    try {
      fileUploader.storeFile(toStoreFileRequestDto(report));
      log.info(
          "Report has been successfully saved in Minio, reportName={}", report.getReportName());

    } catch (Exception e) {
      throw new ReportNotSavedException("Report has not been successfully saved.", e);
    }
  }

  @Override
  public void removeReport(String reportName) {
    fileRemover.removeFile(bucketName, reportName);
  }

  @Override
  public FileDto getReport(String reportName) {
    return fileRetriever.getFile(bucketName, reportName);
  }

  @Override
  public void removeReports(List<String> reportNames) {
    reportNames.forEach(this::removeReport);
  }

  private StoreFileRequestDto toStoreFileRequestDto(Report report) throws FileNotFoundException {
    return StoreFileRequestDto.builder()
        .storageName(bucketName)
        .fileName(report.getReportName())
        .fileContent(report.getInputStream())
        .fileSize(UNKNOWN_FILE_SIZE)
        .build();
  }
}
