package com.silenteight.serp.governance.file.domain.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;
import java.util.UUID;

@Value
@Builder
public class FileReferenceDto {

  @NonNull
  UUID fileId;
  @NonNull
  String fileName;
  @NonNull
  Long fileSize;
  @NonNull
  String uploaderName;
  @NonNull
  LocalDate uploadDate;
  @NonNull
  String mimeType;
}
