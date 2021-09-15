package com.silenteight.payments.bridge.svb.learning.reader.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

class CsvFileProviderTestImpl implements CsvFileProvider {
  @Override
  public File getLearningCsv() {
    File file = null;
    Resource resource = new ClassPathResource("learning/SVB_Learn_Jun_1_to_30.csv");
    try {
      file = resource.getFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return file;
  }
}
