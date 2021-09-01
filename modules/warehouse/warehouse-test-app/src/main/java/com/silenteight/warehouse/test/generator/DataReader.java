package com.silenteight.warehouse.test.generator;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Slf4j
class DataReader {

  private static final String COMMENT_PREFIX = "#";

  @Value("classpath:data/alertData.csv")
  private Resource resourceFile;

  List<String> getLines() {
    try (
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(resourceFile.getInputStream()))) {

      return reader
          .lines()
          .filter(line -> !line.startsWith(COMMENT_PREFIX))
          .collect(toList());
    } catch (IOException e) {
      log.error("Cannot open data file.", e);
    }

    return emptyList();
  }
}
