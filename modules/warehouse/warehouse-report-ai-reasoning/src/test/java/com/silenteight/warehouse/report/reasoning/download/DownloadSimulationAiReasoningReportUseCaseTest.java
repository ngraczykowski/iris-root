package com.silenteight.warehouse.report.reasoning.download;

import lombok.SneakyThrows;

import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.warehouse.report.reasoning.download.dto.DownloadAiReasoningReportDto;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static com.silenteight.warehouse.report.reasoning.AiReasoningReportTestFixtures.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.IOUtils.toInputStream;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DownloadSimulationAiReasoningReportUseCaseTest {

  @Mock
  private AiReasoningReportDataQuery reportDataQuery;
  @Mock
  private ReportStorage reportStorageService;
  @InjectMocks
  private DownloadSimulationAiReasoningReportUseCase underTest;

  @SneakyThrows
  @Test
  void shouldReturnDownloadAiReasoningReportDto() {
    // given
    when(reportDataQuery.getAiReasoningReportDto(REPORT_ID)).thenReturn(AI_REASONING_REPORT_DTO);
    when(reportStorageService.getReport(FILE_STORAGE_NAME)).thenReturn(getFileDto());

    // when
    DownloadAiReasoningReportDto dto = underTest.activate(REPORT_ID, ANALYSIS_ID);

    // then
    assertThat(dto.getName()).isEqualTo(SIMULATION_REPORT_FILENAME);
    assertThat(getContentAsString(dto)).isEqualTo(REPORT_CONTENT);
  }

  private String getContentAsString(DownloadAiReasoningReportDto dto) throws IOException {
    return new String(dto.getContent().readAllBytes(), UTF_8);
  }

  private FileDto getFileDto() {
    return FileDto.builder()
        .name(FILE_STORAGE_NAME)
        .content(toInputStream(REPORT_CONTENT, UTF_8))
        .build();
  }
}
