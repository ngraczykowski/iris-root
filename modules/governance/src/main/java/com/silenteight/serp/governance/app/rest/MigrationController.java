package com.silenteight.serp.governance.app.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.common.rest.RestConstants;
import com.silenteight.serp.governance.migrate.ExportService;
import com.silenteight.serp.governance.migrate.ImportService;
import com.silenteight.serp.governance.migrate.dto.ExportMatchGroupsRequest;
import com.silenteight.serp.governance.migrate.dto.ExportMatchGroupsRequest.ExportMatchGroupsRequestBuilder;
import com.silenteight.serp.governance.migrate.dto.ImportedDecisionTree;

import com.google.common.io.BaseEncoding;
import com.google.protobuf.ByteString;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping(RestConstants.ROOT)
@RequiredArgsConstructor
@Slf4j
class MigrationController {

  private final ImportService importService;
  private final ExportService exportService;

  @PostMapping("/v1/migrate/decision-tree")
  public ResponseEntity<ImportedDecisionTree> importDecisionTree(
      @RequestParam("file") MultipartFile file) throws IOException {

    log.info("Start processing");
    ImportedDecisionTree importedDecisionTree = importService.importTree(file.getInputStream());
    log.info(
        "Imported decision tree: id: {}, name {}", importedDecisionTree.getId(),
        importedDecisionTree.getName());

    return ResponseEntity.ok(importedDecisionTree);
  }

  @GetMapping("/v1/migrate/decision-tree/{nameOrId}")
  public ResponseEntity<Map<String, Object>> exportDecisionTree(@PathVariable String nameOrId) {
    // TODO(ahaczewski): Implement exporting of decision tree.
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  @GetMapping("/v1/migrate/match-groups/{featuresSignature}")
  public ResponseEntity<Map<String, Object>> exportMatchGroups(
      @PathVariable String featuresSignature,
      @RequestParam(name = "tree", required = false) String decisionTreeName) {

    ByteString signatureBytes = toByteString(featuresSignature);

    ExportMatchGroupsRequestBuilder request = ExportMatchGroupsRequest
        .builder()
        .featuresSignature(signatureBytes);

    if (StringUtils.isNotBlank(decisionTreeName))
      request.decisionTreeName(decisionTreeName.trim());

    Map<String, Object> matchGroups = exportService.exportMatchGroups(request.build());

    return ResponseEntity.ok(matchGroups);
  }

  @NotNull
  private static ByteString toByteString(String featuresSignature) {
    return ByteString.copyFrom(BaseEncoding.base64Url().decode(featuresSignature));
  }
}
