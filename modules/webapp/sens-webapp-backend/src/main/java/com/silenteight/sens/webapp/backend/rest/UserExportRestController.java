package com.silenteight.sens.webapp.backend.rest;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.backend.RestConstants;
import com.silenteight.sens.webapp.backend.support.CsvResponseWriter;
import com.silenteight.sens.webapp.common.adapter.audit.AuditService;
import com.silenteight.sens.webapp.common.support.csv.CsvBuilder;
import com.silenteight.sens.webapp.user.dto.UserView;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping(RestConstants.ROOT)
@PreAuthorize("hasAnyAuthority('USER_VIEW', 'AUDIT_GENERATE_REPORT')")
public class UserExportRestController {

  private static final String USERS_FILENAME = "users.csv";

  private final AuditService<UserView> userAuditService;

  private final CsvResponseWriter csvResponseWriter = new CsvResponseWriter();

  @GetMapping("/users/export")
  public void exportUsers(HttpServletResponse response) throws IOException {
    CsvBuilder<UserView> builder = userAuditService.generateAuditReport();
    csvResponseWriter.write(response, USERS_FILENAME, builder);
  }
}
