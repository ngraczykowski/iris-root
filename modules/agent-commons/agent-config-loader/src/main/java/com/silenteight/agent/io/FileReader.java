package com.silenteight.agent.io;

import com.silenteight.agent.configloader.ConfigsPathFinder;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Set;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toSet;

public class FileReader {

  /**
   * Load lines from file located under AGNET_HOME/conf/{filePath}
   *
   * @return Unique and not empty lines
   */
  public static Set<String> loadLinesFromFile(String filePath) {

    try (
        InputStream is = new FileInputStream(
            ConfigsPathFinder.findFile(filePath).toFile())) {
      return IOUtils.readLines(is, UTF_8)
          .stream()
          .filter(StringUtils::isNotEmpty)
          .collect(toSet());
    } catch (Exception e) {
      throw new DictionaryLoadingException(filePath, e);
    }
  }

  public static class DictionaryLoadingException extends RuntimeException {

    private static final long serialVersionUID = 3489265537823339958L;

    public DictionaryLoadingException(
        String filePath, Exception e) {
      super(format("Could not load dictionary %s", filePath), e);
    }
  }
}
