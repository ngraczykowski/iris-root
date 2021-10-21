package com.silenteight.warehouse.report.rbs.download;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.report.rbs.domain.RbsReportService;
import com.silenteight.warehouse.report.rbs.domain.dto.ReportDto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.MODEL_TUNER;
import static com.silenteight.warehouse.report.rbs.RbsReportTestFixtures.REPORT_FILENAME;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

@Import({
    DownloadRbsReportRestController.class,
    DownloadRbsReportConfiguration.class
})
class DownloadReportRestControllerTest extends BaseRestControllerTest {

  private static final long REPORT_ID = 5;
  public static final String CONTENT = "report_content";

  @MockBean
  RbsReportDataQuery query;
  @MockBean
  RbsReportService reportService;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenDownloadingReport() {
    when(query.getReport(REPORT_ID)).thenReturn(ReportDto.of(REPORT_FILENAME, CONTENT));

    String expectedContentDisposition = format("attachment; filename=\"%s\"", REPORT_FILENAME);
    String response = get("/v2/analysis/production/reports/RB_SCORER/" + REPORT_ID)
        .statusCode(OK.value())
        .contentType("text/csv")
        .header("Content-Disposition", expectedContentDisposition)
        .extract()
        .body()
        .asString();

    assertThat(response).isEqualTo(CONTENT);
    verify(reportService).removeReport(REPORT_ID);
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenDownloadingSimReport() {
    when(query.getReport(REPORT_ID)).thenReturn(ReportDto.of(REPORT_FILENAME, CONTENT));

    String expectedContentDisposition = format("attachment; filename=\"%s\"", REPORT_FILENAME);
    String response = get("/v2/analysis/123/reports/RB_SCORER/" + REPORT_ID)
        .statusCode(OK.value())
        .contentType("text/csv")
        .header("Content-Disposition", expectedContentDisposition)
        .extract()
        .body()
        .asString();

    assertThat(response).isEqualTo(CONTENT);
    verify(reportService).removeReport(REPORT_ID);
  }
}
