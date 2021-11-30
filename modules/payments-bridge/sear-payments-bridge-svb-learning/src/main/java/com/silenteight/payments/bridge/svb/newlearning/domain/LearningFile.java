package com.silenteight.payments.bridge.svb.newlearning.domain;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class LearningFile {

  private Long learningFileId;
  private String fileName;
  private String bucketName;
  private CsvProcessingFileStatus status;
  private OffsetDateTime createdAt;

}
