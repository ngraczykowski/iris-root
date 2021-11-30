package com.silenteight.payments.bridge.svb.newlearning.batch;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningRequest;
import com.silenteight.payments.bridge.svb.newlearning.batch.exceptions.NoCsvFileResourceFound;
import com.silenteight.payments.bridge.svb.newlearning.domain.DeleteLearningFileRequest;
import com.silenteight.payments.bridge.svb.newlearning.port.CsvFileResourceProvider;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
@Slf4j
class DefaultCsvFileResourceProvider implements CsvFileResourceProvider {

  private static final String CLASSPATH_LEARNING_PROVIDER = "classpath:learning/provider/";

  @Override
  public Resource getResource(LearningRequest learningRequest) {
    try {
      File file =
          ResourceUtils.getFile(CLASSPATH_LEARNING_PROVIDER + learningRequest.getObject());
      return new InputStreamResource(new FileInputStream(file));
    } catch (IOException e) {
      throw new NoCsvFileResourceFound(
          String.format("No csv file resource found %s", learningRequest.getObject()));
    }
  }

  @Override
  public void deleteLearningFile(
      DeleteLearningFileRequest deleteLearningFileRequest) {
    log.info("Ignore file deletion with DefaultCsvFileResourceProvider implementation");
  }
}
