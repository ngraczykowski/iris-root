package com.silenteight.warehouse.report.billing.v1.download;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.report.billing.v1.domain.DeprecatedBillingReportService;
import com.silenteight.warehouse.report.billing.v1.domain.dto.ReportDto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.MODEL_TUNER;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

@Import(DeprecatedDownloadBillingReportRestController.class)
class DeprecatedDownloadReportRestControllerTest extends BaseRestControllerTest {

  private static final long REPORT_ID = 5;
  public static final String FILE_NAME = "BILLING_FILENAME";
  public static final String CONTENT = "report_content";

  @MockBean
  DeprecatedReportDataQuery query;
  @MockBean
  DeprecatedBillingReportService reportService;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenDownloadingReport() {
    when(query.getReport(REPORT_ID)).thenReturn(ReportDto.of(FILE_NAME, CONTENT));

    String expectedContentDisposition = format("attachment; filename=\"%s\"", FILE_NAME);
    String response = get(
        "/v1/analysis/production/definitions/BILLING/3aa046a1-6c0f-4ac2-bd79-635147db1e01/"
            + "reports/" + REPORT_ID)
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
