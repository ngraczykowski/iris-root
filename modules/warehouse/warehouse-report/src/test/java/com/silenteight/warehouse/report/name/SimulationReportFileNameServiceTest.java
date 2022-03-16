package com.silenteight.warehouse.report.name;

import com.silenteight.warehouse.report.persistence.ReportFileExtension;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;

class SimulationReportFileNameServiceTest {

  @ParameterizedTest
  @MethodSource("provideDataForGetReportName")
  void shouldGetReportName(String pattern, ReportFileNameDto dto, String expectedReportName) {
    // given
    ReportFileName reportFileName = new SimulationReportFileNameService(pattern);

    // when
    String actualReportName = reportFileName.getReportName(dto);

    // then
    assertThat(actualReportName).isEqualTo(expectedReportName);
  }

  private static Stream<Arguments> provideDataForGetReportName() {
    return Stream.of(
        of(
            "[reportType]_Sim_[analysisId].[extension]",
            toReportFileNameDto("Accuracy", "ad651a4d-e398-4e8f-b451-4c23c12ea51f", "1234123213",
                ReportFileExtension.CSV.getFileExtension()),
            "Accuracy_Sim_ad651a4d-e398-4e8f-b451-4c23c12ea51f.csv"),
        of(
            "[reportType]_Sim_[analysisId]_[timestamp].[extension]",
            toReportFileNameDto("Billing", "4471fdec-3aec-42ff-aa9d-743f2e0cbdfc", "1234123215",
                ReportFileExtension.CSV.getFileExtension()),
            "Billing_Sim_4471fdec-3aec-42ff-aa9d-743f2e0cbdfc_1234123215.csv"),
        of(
            "Simple_report.[extension]",
            toReportFileNameDto("Billing", "ad651a4d-e398-4e8f-b451-4c23c12ea51f", "1234123213",
                ReportFileExtension.CSV.getFileExtension()),
            "Simple_report.csv"),
        of(
            "[reportType]_Sim_[analysisId].[extension]",
            ReportFileNameDto.builder().build(),
            "_Sim_."),
        of(
            "[reportType]_Sim_[analysisId].[extension]",
            toReportFileNameDto("Accuracy", "ad651a4d-e398-4e8f-b451-4c23c12ea51f", "1234123213",
                ReportFileExtension.CSV.getFileExtension()),
            "Accuracy_Sim_ad651a4d-e398-4e8f-b451-4c23c12ea51f.csv"),
        of(
            "[reportType]_Sim_[analysisId].[extension]",
            toReportFileNameDto("Accuracy", "ad651a4d-e398-4e8f-b451-4c23c12ea51f", "1234123213",
                ReportFileExtension.ZIP.getFileExtension()),
            "Accuracy_Sim_ad651a4d-e398-4e8f-b451-4c23c12ea51f.zip"));
  }

  private static ReportFileNameDto toReportFileNameDto(
      String reportType, String analysisId, String timestamp, String extension) {

    return ReportFileNameDto.builder()
        .reportType(reportType)
        .analysisId(analysisId)
        .timestamp(timestamp)
        .extension(extension)
        .build();
  }
}
