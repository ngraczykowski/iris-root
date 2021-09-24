package com.silenteight.serp.governance.file.upload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadRequestDto {

  @Nullable
  MultipartFile file;
}
