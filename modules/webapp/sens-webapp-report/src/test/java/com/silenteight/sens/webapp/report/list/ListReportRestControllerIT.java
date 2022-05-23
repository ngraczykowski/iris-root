package com.silenteight.sens.webapp.report.list;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.AUDITOR;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.QA;
import static java.util.List.of;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import(ListReportRestController.class)
class ListReportRestControllerIT extends BaseRestControllerTest {

  private static final ReportDto REPORT_DTO_1 = ReportDto.builder()
      .name("audit")
      .label("Audit report")
      .filter(FilterType.NONE)
      .build();

  private static final ReportDto REPORT_DTO_2 = ReportDto.builder()
      .name("user")
      .label("User report")
      .filter(FilterType.DATE_RANGE)
      .build();

  @MockBean
  ListReportsQuery listReportsQuery;

  @Test
  @WithMockUser(username = USERNAME, authorities = QA)
  void its403_whenNotPermittedRoleForListing() {
    get("/reports").statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = AUDITOR)
  void its200_whenPermittedRoleForListing() {
    when(listReportsQuery.listAll()).thenReturn(of(REPORT_DTO_1, REPORT_DTO_2));
    get("/reports")
        .statusCode(OK.value())
        .body("[0].name", is(REPORT_DTO_1.getName()))
        .body("[0].label", is(REPORT_DTO_1.getLabel()))
        .body("[0].filter", is(REPORT_DTO_1.getFilter().name()))
        .body("[1].name", is(REPORT_DTO_2.getName()))
        .body("[1].label", is(REPORT_DTO_2.getLabel()))
        .body("[1].filter", is(REPORT_DTO_2.getFilter().name()))
    ;
  }
}
