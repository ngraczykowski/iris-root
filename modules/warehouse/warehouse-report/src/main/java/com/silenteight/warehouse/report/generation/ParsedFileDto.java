package com.silenteight.warehouse.report.generation;

import lombok.Builder;
import lombok.Value;

import java.io.File;

@Value
@Builder
class ParsedFileDto {

  File file;
  boolean zipped;
}
