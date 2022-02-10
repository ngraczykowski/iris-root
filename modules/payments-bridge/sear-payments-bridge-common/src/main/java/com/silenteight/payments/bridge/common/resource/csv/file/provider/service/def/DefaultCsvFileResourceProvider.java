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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

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
    try {
      return Stream.of(
              Objects.requireNonNull(
                  ResourceUtils.getFile(CLASSPATH_LEARNING_PROVIDER).listFiles()))
          .filter(file -> !file.isDirectory())
          .map(File::getName)
          .map(name -> ObjectPath.builder().name(name).bucket("").build())
          .collect(toList());
    } catch (FileNotFoundException e) {
      log.error("Couldn't find any files in following path = {}", CLASSPATH_LEARNING_PROVIDER);
    }

    return List.of();
  }
}
