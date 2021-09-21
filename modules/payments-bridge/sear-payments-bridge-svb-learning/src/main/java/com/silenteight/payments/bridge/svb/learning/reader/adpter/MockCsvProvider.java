package com.silenteight.payments.bridge.svb.learning.reader.adpter;

import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningCsv;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningRequest;
import com.silenteight.payments.bridge.svb.learning.reader.port.CsvFileProvider;

import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;

@Service
@Profile("mockaws")
class MockCsvProvider implements CsvFileProvider {

  @Override
  public LearningCsv getLearningCsv(LearningRequest learningRequest) {
    Resource resource = new ClassPathResource("learning/SVB_Learn_Jun_1_to_30.csv");
    try {
      var file = resource.getFile();
      return LearningCsv.builder().content(new FileInputStream(file)).build();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
