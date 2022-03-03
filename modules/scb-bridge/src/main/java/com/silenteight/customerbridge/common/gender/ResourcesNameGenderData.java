package com.silenteight.customerbridge.common.gender;

import lombok.Data;
import lombok.NonNull;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import static java.util.Collections.unmodifiableList;

@Data
public class ResourcesNameGenderData implements NameGenderData {

  private static final String MALE_NAMES_LOCATION =
      "gender/male-names.csv";
  private static final String FEMALE_NAMES_LOCATION =
      "gender/female-names.csv";

  private final List<String> maleNames;
  private final List<String> femaleNames;

  protected ResourcesNameGenderData(
      @NonNull String maleNamesLocation,
      @NonNull String femaleNamesLocation) {

    this.maleNames = readFile(maleNamesLocation);
    this.femaleNames = readFile(femaleNamesLocation);
  }

  private List<String> readFile(@NonNull String resource) {
    try (InputStream fis = getClass().getClassLoader().getResourceAsStream(resource)) {
      if (fis == null)
        throw new InvalidResourceException(resource);

      return IOUtils.readLines(fis, Charset.forName("UTF-8"));
    } catch (IOException e) {
      throw new ReadingNamesFromResourceFailedException(resource, e);
    }
  }

  static NameGenderData create() {
    return new ResourcesNameGenderData(MALE_NAMES_LOCATION, FEMALE_NAMES_LOCATION);
  }

  public List<String> getMaleNames() {
    return unmodifiableList(maleNames);
  }

  public List<String> getFemaleNames() {
    return unmodifiableList(femaleNames);
  }

  public static final class InvalidResourceException extends IllegalArgumentException {

    private static final long serialVersionUID = 8135956851702479974L;

    InvalidResourceException(String resource) {
      super("Resource '" + resource + "' is invalid");
    }
  }

  public static final class ReadingNamesFromResourceFailedException extends RuntimeException {

    private static final long serialVersionUID = -7396973592911045061L;

    ReadingNamesFromResourceFailedException(String resource, Throwable cause) {
      super("Failed reading names from '" + resource + "'", cause);
    }
  }
}
