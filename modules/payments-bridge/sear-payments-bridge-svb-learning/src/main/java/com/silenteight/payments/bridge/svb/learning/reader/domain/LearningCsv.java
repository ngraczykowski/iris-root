package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Builder;
import lombok.Value;

import com.amazonaws.services.s3.model.S3Object;

import java.io.InputStream;

@Value
@Builder
public class LearningCsv {

  String fileName;

  String hash;

  long fileLength;

  InputStream content;

  public static LearningCsv fromS3Object(S3Object object) {
    var metadata = object.getObjectMetadata();
    return LearningCsv
        .builder()
        .fileName(object.getKey())
        .fileLength(metadata.getContentLength())
        .hash(metadata.getETag())
        .content(object.getObjectContent())
        .build();
  }
}
