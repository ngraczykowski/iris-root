package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Builder;
import lombok.Value;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.InputStream;

@Value
@Builder
public class LearningCsv {

  String fileName;

  String hash;

  long fileLength;

  InputStream content;

  public static LearningCsv fromS3Object(
      String fileName, ResponseInputStream<GetObjectResponse> responseInputStream) {

    var response = responseInputStream.response();

    return LearningCsv
        .builder()
        .fileName(fileName)
        .fileLength(response.contentLength())
        .hash(response.eTag())
        .content(responseInputStream)
        .build();
  }
}
