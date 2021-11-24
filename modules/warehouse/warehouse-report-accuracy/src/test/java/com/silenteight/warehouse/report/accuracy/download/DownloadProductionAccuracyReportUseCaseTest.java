package com.silenteight.warehouse.report.accuracy.download;

import lombok.SneakyThrows;

import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.warehouse.report.accuracy.download.dto.DownloadAccuracyReportDto;
import com.silenteight.warehouse.report.name.ReportFileName;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static com.silenteight.warehouse.report.accuracy.AccuracyReportTestFixtures.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.IOUtils.toInputStream;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DownloadProductionAccuracyReportUseCaseTest {

  @Mock
  private AccuracyReportDataQuery reportDataQuery;
  @Mock
  private ReportStorage reportStorageService;
  @Mock
  private ReportFileName reportFileName;

  private DownloadProductionAccuracyReportUseCase underTest;

  @BeforeEach
  void setUp() {
    underTest = new DownloadAccuracyReportConfiguration().downloadProductionAccuracyReportUseCase(
        reportDataQuery, reportStorageService, reportFileName);
  }

  @SneakyThrows
  @Test
  void shouldReturnDownloadAccuracyReportDto() {
    // given
    when(reportDataQuery.getAccuracyReportDto(REPORT_ID)).thenReturn(ACCURACY_REPORT_DTO);
    when(reportStorageService.getReport(FILE_STORAGE_NAME)).thenReturn(getFileDto());
    when(reportFileName.getReportName(any())).thenReturn(PRODUCTION_REPORT_FILENAME);

    // when
    DownloadAccuracyReportDto dto = underTest.activate(REPORT_ID);

    // then
    assertThat(dto.getName()).isEqualTo(PRODUCTION_REPORT_FILENAME);
    assertThat(getContentAsString(dto)).isEqualTo(REPORT_CONTENT);
  }

  private String getContentAsString(DownloadAccuracyReportDto dto) throws IOException {
    return new String(dto.getContent().readAllBytes(), UTF_8);
  }

  private FileDto getFileDto() {
    return FileDto.builder()
        .name(FILE_STORAGE_NAME)
        .content(toInputStream(REPORT_CONTENT, UTF_8))
        .build();
  }
}
