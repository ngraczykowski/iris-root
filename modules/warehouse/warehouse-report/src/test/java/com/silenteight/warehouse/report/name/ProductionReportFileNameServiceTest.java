package com.silenteight.warehouse.report.name;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;

class ProductionReportFileNameServiceTest {

  @ParameterizedTest
  @MethodSource("provideDataForGetReportName")
  void shouldGetReportName(String pattern, ReportFileNameDto dto, String expectedReportName) {
    // given
    ReportFileName reportFileName = new ProductionReportFileNameService(pattern);

    // when
    String actualReportName = reportFileName.getReportName(dto);

    // then
    assertThat(actualReportName).isEqualTo(expectedReportName);
  }

  private static Stream<Arguments> provideDataForGetReportName() {
    return Stream.of(
        of(
            "[reportType]_Prod_[from]_To_[to].csv",
            toReportFileNameDto("Accuracy", "2020-01-01", "2022-01-01", "1234123213"),
            "Accuracy_Prod_2020-01-01_To_2022-01-01.csv"),
        of(
            "[reportType]_Prod_[from]_To_[to]_[timestamp].csv",
            toReportFileNameDto("AI_Reasoning", "2021-01-01", "2023-01-01", "1234123215"),
            "AI_Reasoning_Prod_2021-01-01_To_2023-01-01_1234123215.csv"),
        of(
            "Simple_report.csv",
            toReportFileNameDto("Accuracy", "2020-01-01", "2022-01-01", "1234123213"),
            "Simple_report.csv"),
        of(
            "[reportType]_Prod_[from]_To_[to].csv",
            ReportFileNameDto.builder().build(),
            "_Prod__To_.csv")
    );
  }

  private static ReportFileNameDto toReportFileNameDto(
      String reportType, String from, String to, String timestamp) {

    return ReportFileNameDto.builder()
        .reportType(reportType)
        .from(from)
        .to(to)
        .timestamp(timestamp)
        .build();
  }
}
