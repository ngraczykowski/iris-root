package com.silenteight.warehouse.report.storage;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.io.InputStream;

@Data
@Builder
public class FileDto {

  @NonNull
  private InputStream content;

  @NonNull
  private String name;
}
