package com.silenteight.payments.bridge.svb.learning.reader.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
class CsvProviderImpl implements CsvFileProvider {

  @Override
  public File getLearningCsv() {
    File file;
    Resource resource = new ClassPathResource("learning/provider/SVB_Learn_Jun_1_to_30.csv");
    try {
      file = resource.getFile();
    } catch (IOException e) {
      throw new RuntimeException("couldn't find file");
    }
    return file;
  }
}
