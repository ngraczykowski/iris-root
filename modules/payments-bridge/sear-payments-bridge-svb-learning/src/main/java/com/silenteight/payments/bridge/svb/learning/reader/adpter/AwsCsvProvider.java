package com.silenteight.payments.bridge.svb.learning.reader.adpter;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningCsv;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningRequest;
import com.silenteight.payments.bridge.svb.learning.reader.port.CsvFileProvider;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

@Service
@Slf4j
@Profile("!mockaws")
class AwsCsvProvider implements CsvFileProvider {

  @Override
  public LearningCsv getLearningCsv(LearningRequest learningRequest) {

    log.info("Sending request to S3");

    // FIXME(ahaczewski): This needs to be CLOSED!!!
    var s3Client = S3Client.builder().build();

    var responseInputStream = s3Client.getObject(
        GetObjectRequest
            .builder()
            .bucket(learningRequest.getBucket())
            .key(learningRequest.getObject())
            .build());

    log.info("Received S3 CVS object");

    return LearningCsv.fromS3Object(learningRequest.getObject(), responseInputStream);
  }
}
