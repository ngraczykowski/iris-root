package com.silenteight.warehouse.report.billing.download;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.report.billing.download.dto.DownloadBillingReportDto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.MODEL_TUNER;
import static com.silenteight.warehouse.report.billing.BillingReportTestFixtures.CONTENT;
import static com.silenteight.warehouse.report.billing.BillingReportTestFixtures.REPORT_FILENAME;
import static com.silenteight.warehouse.report.billing.BillingReportTestFixtures.REPORT_ID;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

@Import({
    DownloadBillingReportRestController.class,
    DownloadBillingReportConfiguration.class
})
@TestPropertySource(properties = "warehouse.reports.billing=true")
class DownloadReportRestControllerTest extends BaseRestControllerTest {

  @MockBean
  DownloadBillingReportUseCase useCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenDownloadingReport() {
    when(useCase.activate(REPORT_ID)).thenReturn(createDownloadBillingReportDto());

    String expectedContentDisposition = format("attachment; filename=\"%s\"", REPORT_FILENAME);
    String response = get("/v2/analysis/production/reports/BILLING/" + REPORT_ID)
        .statusCode(OK.value())
        .contentType("text/csv")
        .header("Content-Disposition", expectedContentDisposition)
        .extract()
        .body()
        .asString();

    assertThat(response).isEqualTo(CONTENT);
  }

  private DownloadBillingReportDto createDownloadBillingReportDto() {
    return DownloadBillingReportDto.builder()
        .name(REPORT_FILENAME)
        .content(CONTENT)
        .build();
  }
}
