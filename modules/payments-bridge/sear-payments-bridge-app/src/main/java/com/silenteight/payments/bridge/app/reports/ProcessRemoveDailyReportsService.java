package com.silenteight.payments.bridge.app.reports;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.resource.csv.file.provider.model.DeleteFileRequest;
import com.silenteight.payments.bridge.common.resource.csv.file.provider.model.FileDetails;
import com.silenteight.payments.bridge.common.resource.csv.file.provider.model.FilesListPatternRequest;
import com.silenteight.payments.bridge.common.resource.csv.file.provider.port.CsvFileResourceProvider;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
class ProcessRemoveDailyReportsService implements ProcessRemoveDailyReportsUseCase {

  private final DailyReportsProperties dailyReportsProperties;
  private final CsvFileResourceProvider resourceProvider;

  @Override
  public void processRemoveDailyReports() {

    List<FileDetails> filesListBasedOnPattern = getFilesDetails();

    var localDateSixMonthsToNow =
        LocalDate.now().minusMonths(dailyReportsProperties.getReportExpirationInMonths());

    var filesToDelete = filterFileDetailsToDelete(filesListBasedOnPattern,
        localDateSixMonthsToNow);

    deleteFiles(filesToDelete);
  }

  private List<FileDetails> getFilesDetails() {
    var filesListPatternRequest = FilesListPatternRequest.builder()
        .bucket(dailyReportsProperties.getBucket())
        .filePattern(dailyReportsProperties.getFilePrefix())
        .build();

    log.debug("FilesListPatternRequest={}", filesListPatternRequest);

    List<FileDetails> filesListBasedOnPattern =
        resourceProvider.getFilesListBasedOnPattern(filesListPatternRequest);
    return filesListBasedOnPattern;
  }

  private List<FileDetails> filterFileDetailsToDelete(List<FileDetails> filesListBasedOnPattern,
      LocalDate localDateSixMonthsToNow) {
    var filesToDelete = filesListBasedOnPattern.stream()
        .filter(file -> checkIfFileIsObsoleted(localDateSixMonthsToNow, file.getLastModified()))
        .collect(Collectors.toList());

    log.debug("Files to delete = {}", filesToDelete);

    return filesToDelete;
  }

  private static boolean checkIfFileIsObsoleted(
      LocalDate localDateSixMonthsToNow, Instant reportDate) {
    return localDateSixMonthsToNow.isAfter(LocalDate.ofInstant(
        reportDate,
        ZonedDateTime.now().getZone()));
  }

  private void deleteFiles(List<FileDetails> filesToDelete) {
    filesToDelete.forEach(fileDetails -> resourceProvider.deleteFile(DeleteFileRequest.builder()
        .bucket(fileDetails.getBucket())
        .object(fileDetails.getName())
        .build()));

    log.debug("Files ware deleted = {}", filesToDelete);
  }
}
