package com.silenteight.warehouse.report.download;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

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
@AllArgsConstructor
@RequestMapping(ROOT)
public class DownloadReportRestController {

  private static final String COMMON_REPORT_URL = "v3/analysis/{type}/reports/{name}/{id}";

  @NonNull
  private final DownloadService downloadService;

  @GetMapping(COMMON_REPORT_URL)
  @PreAuthorize("isAuthorized('DOWNLOAD_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<Resource> downloadReport(
      @PathVariable String type,
      @PathVariable String name,
      @PathVariable long id
  ) {

    log.info("Download {} report on demand request received, reportId={}", name, id);

    DownloadReportDto dto = downloadService.getFor(type, id);

    log.debug(
        "Download {} report request processed, reportId={}, reportName={}",
        name,
        id,
        dto.getName());

    return ok()
        .header(CONTENT_DISPOSITION, getContentDisposition(dto.getName()))
        .contentType(APPLICATION_OCTET_STREAM)
        .body(new InputStreamResource(dto.getContent()));
  }

  private static String getContentDisposition(String filename) {
    return format("attachment; filename=\"%s\"", filename);
  }

}
