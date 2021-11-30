package com.silenteight.payments.bridge.svb.newlearning.batch;

import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningRequest;
import com.silenteight.payments.bridge.svb.newlearning.batch.exceptions.NoCsvFileResourceFound;
import com.silenteight.payments.bridge.svb.newlearning.port.CsvFileResourceProvider;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
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
}
