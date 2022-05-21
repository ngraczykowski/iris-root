package com.silenteight.hsbc.bridge.aws;

import lombok.experimental.UtilityClass;

import java.net.URI;

@UtilityClass
class AwsUriUtils {

  static String getObjectKey(URI uri) {
    String[] path = uri.getPath().split("/");
    return path[path.length - 1];
  }
}
