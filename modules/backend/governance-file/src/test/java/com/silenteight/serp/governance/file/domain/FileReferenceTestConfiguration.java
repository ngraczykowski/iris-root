package com.silenteight.serp.governance.file.domain;

import com.silenteight.sep.filestorage.api.FileRemover;
import com.silenteight.sep.filestorage.api.FileRetriever;
import com.silenteight.sep.filestorage.api.FileUploader;
import com.silenteight.serp.governance.file.FileModule;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
    FileModule.class
})
public class FileReferenceTestConfiguration {

  @MockBean
  FileRetriever fileRetriever;

  @MockBean
  FileUploader fileUploader;

  @MockBean
  FileRemover fileRemover;
}
