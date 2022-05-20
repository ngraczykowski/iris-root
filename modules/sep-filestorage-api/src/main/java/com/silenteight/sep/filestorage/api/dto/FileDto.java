
package com.silenteight.sep.filestorage.api.dto;

import lombok.Builder;
import lombok.Value;

import java.io.InputStream;

@Value
@Builder
public class FileDto {

  String name;

  InputStream content;

  long sizeInBytes;
}
