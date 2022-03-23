
package com.silenteight.payments.bridge.common.resource.csv.file.provider.port;

import com.silenteight.payments.bridge.common.resource.csv.file.provider.model.*;

import org.springframework.core.io.Resource;

import java.util.List;

public interface CsvFileResourceProvider {

  Resource getResource(FileRequest fileRequest);

  void deleteFile(DeleteFileRequest deleteFileRequest);

  List<ObjectPath> getFilesList();

  List<FileDetails> getFilesListBasedOnPattern(FilesListPatternRequest filesListPatternRequest);
}
