package com.silenteight.sep.filestorage.api.dto;

import lombok.Builder;
import lombok.Value;

import java.io.InputStream;

@Value
@Builder
public class StoreFileRequestDto {

  String fileName;

  InputStream fileContent;

  long fileSize;
}
