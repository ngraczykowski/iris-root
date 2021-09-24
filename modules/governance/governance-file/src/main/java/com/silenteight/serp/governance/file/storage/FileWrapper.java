package com.silenteight.serp.governance.file.storage;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class FileWrapper {

  @NonNull
  String fileName;
  @NonNull
  String mimeType;
  @NonNull
  byte[] content;
}
