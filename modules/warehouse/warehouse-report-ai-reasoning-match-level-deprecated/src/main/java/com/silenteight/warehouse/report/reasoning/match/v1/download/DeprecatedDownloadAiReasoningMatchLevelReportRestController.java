package com.silenteight.warehouse.report.reasoning.match.v1.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.filestorage.api.dto.FileDto;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static java.lang.String.format;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ROOT)
class DeprecatedDownloadAiReasoningMatchLevelReportRestController {

  private static final String DEFINITION_ID_PARAM = "definitionId";
  private static final String ID_PARAM = "id";
  private static final String DOWNLOAD_PRODUCTION_REPORT_URL =
      "/v1/analysis/production/definitions/AI_REASONING_MATCH_LEVEL/{definitionId}/reports/{id}";

  @NonNull
  private final DeprecatedDownloadAiReasoningMatchLevelReportUseCase useCase;

  @GetMapping(DOWNLOAD_PRODUCTION_REPORT_URL)
  @PreAuthorize("isAuthorized('DOWNLOAD_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<Resource> downloadProductionReport(
      @PathVariable(DEFINITION_ID_PARAM) String definitionId,
      @PathVariable(ID_PARAM) long id) {

    log.info("Download production AI Reasoning Match Level report on demand request received, "
        + "reportId={}", id);

    FileDto fileDto = useCase.activate(id);

    log.debug("Download production AI Reasoning Match Level report request processed, reportId={}, "
        + "reportName={}", id, fileDto.getName());

    return ok()
        .header(CONTENT_DISPOSITION, format("attachment; filename=%s", fileDto.getName()))
        .contentType(APPLICATION_OCTET_STREAM)
        .body(new InputStreamResource(fileDto.getContent()));
  }
}
