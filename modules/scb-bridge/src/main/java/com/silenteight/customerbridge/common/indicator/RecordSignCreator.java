package com.silenteight.customerbridge.common.indicator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Base64;

import static org.apache.commons.codec.digest.DigestUtils.sha1;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecordSignCreator {

  public static String fromSourceDetails(@NonNull String sourceDetails) {
    String standardizedValue = SourceDetailsCleaner.clean(sourceDetails);
    return encode(standardizedValue);
  }

  public static String fromRecord(@NonNull String record, char separator) {
    String standardizedValue = RecordDetailsCleaner.clean(record, separator);
    return encode(standardizedValue);
  }

  private static String encode(String value) {
    return Base64.getUrlEncoder().encodeToString(sha1(value));
  }
}
