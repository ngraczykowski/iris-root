package com.silenteight.payments.bridge.svb.learning.reader.adpter;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.reader.domain.AlertsReadingResponse;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningCsv;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningRequest;
import com.silenteight.payments.bridge.svb.learning.reader.port.CsvFileProvider;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.util.function.Function;

@Service
@Slf4j
@Profile("!mockaws")
class AwsCsvProvider implements CsvFileProvider {

  @Override
  public AlertsReadingResponse getLearningCsv(
      LearningRequest learningRequest, Function<LearningCsv, AlertsReadingResponse> csvConsumer) {

    log.info("Sending request to S3");

    var s3Client = S3Client.builder().build();

    AlertsReadingResponse response;
    try (
        var responseInputStream = s3Client.getObject(
            GetObjectRequest
                .builder()
                .bucket(learningRequest.getBucket())
                .key(learningRequest.getObject())
                .build())) {

      log.info("Received S3 CVS object");

      response = csvConsumer.apply(
          LearningCsv.fromS3Object(learningRequest.getObject(), responseInputStream));
    } catch (Exception e) {
      log.error("There was a problem when receiving s3 object = " + e.getMessage());
      throw new AwsS3Exception(e);
    }

    s3Client.close();

    return response;
  }

  private static final class AwsS3Exception extends RuntimeException {

    private static final long serialVersionUID = 3289330223618728867L;

    AwsS3Exception(Exception e) {
      super("There was a problem when receiving s3 object = " + e.getMessage());
    }
  }
}
