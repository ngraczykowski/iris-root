package com.silenteight.payments.bridge.app.integration.newlearning;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningRequest;
import com.silenteight.payments.bridge.svb.newlearning.domain.DeleteLearningFileRequest;
import com.silenteight.payments.bridge.svb.newlearning.port.CsvFileResourceProvider;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

@RequiredArgsConstructor
@Slf4j
class S3CsvFileResourceProvider implements CsvFileResourceProvider {

  private final S3Client s3Client;

  @Override
  public Resource getResource(
      LearningRequest learningRequest) {
    var responseInputStream = s3Client.getObject(
        GetObjectRequest
            .builder()
            .bucket(learningRequest.getBucket())
            .key(learningRequest.getObject())
            .build());
    if (log.isDebugEnabled()) {
      log.debug(
          "Getting S3 input stream with contentLength:{}",
          responseInputStream.response().contentLength());
    }
    return new InputStreamResource(responseInputStream);
  }

  @Override
  public void deleteLearningFile(
      DeleteLearningFileRequest deleteLearningFileRequest) {
    s3Client.deleteObject(DeleteObjectRequest.builder()
        .key(deleteLearningFileRequest.getObject())
        .bucket(deleteLearningFileRequest.getBucket())
        .build());
  }

}
