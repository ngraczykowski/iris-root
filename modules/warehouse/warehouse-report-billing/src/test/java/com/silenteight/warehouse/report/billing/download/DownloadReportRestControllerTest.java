package com.silenteight.warehouse.report.billing.download;

import lombok.SneakyThrows;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.report.billing.download.dto.DownloadBillingReportDto;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.io.InputStream;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.MODEL_TUNER;
import static com.silenteight.warehouse.report.billing.BillingReportTestFixtures.CONTENT;
import static com.silenteight.warehouse.report.billing.BillingReportTestFixtures.REPORT_FILENAME;
import static com.silenteight.warehouse.report.billing.BillingReportTestFixtures.REPORT_ID;
import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.IOUtils.toInputStream;
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
  @SneakyThrows
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenDownloadingReport() {
    when(useCase.activate(REPORT_ID)).thenReturn(createDownloadBillingReportDto());

    String expectedContentDisposition = format("attachment; filename=\"%s\"", REPORT_FILENAME);
    InputStream response = get("/v2/analysis/production/reports/BILLING/" + REPORT_ID)
        .statusCode(OK.value())
        .contentType("application/octet-stream")
        .header("Content-Disposition", expectedContentDisposition)
        .extract()
        .body()
        .asInputStream();

    String expectedContent = IOUtils.toString(getContentAsInputStream(), UTF_8);
    String actualContent = IOUtils.toString(response, UTF_8);

    assertThat(actualContent).isEqualTo(expectedContent);
  }

  private DownloadBillingReportDto createDownloadBillingReportDto() {
    return DownloadBillingReportDto.builder()
        .name(REPORT_FILENAME)
        .content(getContentAsInputStream())
        .build();
  }

  private InputStream getContentAsInputStream() {
    return toInputStream(CONTENT, UTF_8);
  }
}
