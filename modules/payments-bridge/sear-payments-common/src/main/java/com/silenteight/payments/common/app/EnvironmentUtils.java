package com.silenteight.payments.common.app;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.System.getProperty;
import static java.lang.System.setProperty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class EnvironmentUtils {

  public static void setPropertyFromEnvironment(String property, String environmentVariable) {
    // XXX(ahaczewski): Refactor that a little.
    // Do not override system properties with environment variables.
    if (getProperty(property) == null) {
      // Read the value from a file pointed by the `<env_var>_FILE` environment variable.
      var environmentVariableFileValue = System.getenv(environmentVariable + "_FILE");
      if (isNotBlank(environmentVariableFileValue)) {
        var variableFilePath = Paths.get(environmentVariableFileValue);
        String variableValue = null;
        try {
          variableValue = Files.readString(variableFilePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
          log.warn(
              "Failed to read variable value from file: variable={}, file={}, error={}, message={}",
              environmentVariable, variableFilePath, e.getClass().getName(), e.getMessage());
        }

        if (isNotBlank(variableValue)) {
          setProperty(property, variableValue);
          return;
        }
      }

      // Read the value directly from the environment variable.
      var environmentVariableValue = System.getenv(environmentVariable);
      if (isNotBlank(environmentVariableValue))
        setProperty(property, environmentVariableValue);
    }
  }
}
