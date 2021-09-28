package com.silenteight.serp.governance.file.description;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.file.domain.dto.FileReferenceDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.sep.auth.authentication.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.*;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class FileDescriptionRestController {

  @NonNull
  private final FileDescriptionQuery fileDescriptionQuery;

  @GetMapping("/v1/files/{id}/description")
  @PreAuthorize("isAuthorized('LIST_ATTACHMENTS')")
  public ResponseEntity<FileReferenceDto> getFileDescription(@PathVariable String id) {

    log.info("Description request for file with id {} received.", id);
    return ok().body(fileDescriptionQuery.get(id));
  }
}
