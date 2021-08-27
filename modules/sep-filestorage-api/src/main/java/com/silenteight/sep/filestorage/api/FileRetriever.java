package com.silenteight.sep.filestorage.api;

import com.silenteight.sep.filestorage.api.dto.FileDto;

public interface FileRetriever {

  FileDto getFile(String fileName,String storageName);
}
