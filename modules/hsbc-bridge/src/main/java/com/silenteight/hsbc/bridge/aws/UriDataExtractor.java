package com.silenteight.hsbc.bridge.aws;

import com.amazonaws.services.s3.AmazonS3URI;

class UriDataExtractor {

  static AmazonS3URI toAwsUri(String uri) {
    return new AmazonS3URI(uri.replaceFirst("http", "s3"));
  }

  static String getObjectKey(AmazonS3URI uri) {
    String temp = uri.getKey().split("/")[1];
    return temp.split("\\?")[0];
  }

  static String getObjectVersion(AmazonS3URI uri) {
    String temp = uri.getKey().split("/")[1];
    return temp.split("\\?")[1].substring(10);
  }
}
