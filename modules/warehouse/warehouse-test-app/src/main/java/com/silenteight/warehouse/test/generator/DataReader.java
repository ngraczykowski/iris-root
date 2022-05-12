package com.silenteight.warehouse.test.generator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class DataReader {

  private static final String COMMENT_PREFIX = "#";

  private final Resource resourceFile;

  public List<String> getLines() {
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
