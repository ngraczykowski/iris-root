package com.silenteight.hsbc.bridge.aws;

import lombok.experimental.UtilityClass;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@UtilityClass
class AwsUriUtils {

  static String getObjectKey(URI uri) {
    String[] path = uri.getPath().split("/");
    return path[path.length - 1];
  }

  static String getObjectVersion(URI uri) {
    var valuePairs = URLEncodedUtils.parse(uri, StandardCharsets.UTF_8);
    var map = valuePairs.stream()
        .collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));
    return map.get("versionId");
  }
}
