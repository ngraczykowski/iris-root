package com.silenteight.sep.filestorage.api;

import com.silenteight.sep.filestorage.api.dto.FileDto;

import java.util.List;

public interface FileRetriever {

  FileDto getFile(String storageName, String fileName);

  List<FileDto> getAllFiles(String storageName);
}
