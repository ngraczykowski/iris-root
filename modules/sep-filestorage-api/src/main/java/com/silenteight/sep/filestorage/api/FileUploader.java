package com.silenteight.sep.filestorage.api;

import com.silenteight.sep.filestorage.api.dto.StoreFileRequestDto;

public interface FileUploader {

  void storeFile(StoreFileRequestDto file, String storageName);
}
