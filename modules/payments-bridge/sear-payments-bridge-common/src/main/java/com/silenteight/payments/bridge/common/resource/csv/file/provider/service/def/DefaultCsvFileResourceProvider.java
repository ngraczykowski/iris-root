package com.silenteight.payments.bridge.common.resource.csv.file.provider.service.def;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.resource.csv.file.provider.exception.NoCsvFileResourceFound;
import com.silenteight.payments.bridge.common.resource.csv.file.provider.model.DeleteLearningFileRequest;
import com.silenteight.payments.bridge.common.resource.csv.file.provider.model.FileRequest;
import com.silenteight.payments.bridge.common.resource.csv.file.provider.model.ObjectPath;
import com.silenteight.payments.bridge.common.resource.csv.file.provider.port.CsvFileResourceProvider;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
class DefaultCsvFileResourceProvider implements CsvFileResourceProvider {

  private static final String CLASSPATH_LEARNING_PROVIDER = "classpath:learning/provider/";

  @Override
  public Resource getResource(FileRequest fileRequest) {
    try {
      File file =
          ResourceUtils.getFile(CLASSPATH_LEARNING_PROVIDER + fileRequest.getObject());
      return new InputStreamResource(new FileInputStream(file));
    } catch (IOException e) {
      throw new NoCsvFileResourceFound(
          String.format("No csv file resource found %s", fileRequest.getObject()));
    }
  }

  @Override
  public void deleteLearningFile(
      DeleteLearningFileRequest deleteLearningFileRequest) {
    log.info("Ignore file deletion with DefaultCsvFileResourceProvider implementation");
  }

  @Override
  public List<ObjectPath> getFilesList() {
    return List.of(ObjectPath.builder().name("analystdecison-2-hits.csv").bucket("").build());
  }
}
