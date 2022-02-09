package com.silenteight.payments.bridge.svb.learning.reader.adapter;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.resource.csv.file.provider.model.FileRequest;
import com.silenteight.payments.bridge.svb.learning.reader.domain.AlertsReadingResponse;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningCsv;
import com.silenteight.payments.bridge.svb.learning.reader.port.CsvFileProvider;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.util.function.Function;

@Service
@Slf4j
@Profile("!mockaws")
class AwsCsvProvider implements CsvFileProvider {

  @Override
  public AlertsReadingResponse getLearningCsv(
      FileRequest learningRequest, Function<LearningCsv, AlertsReadingResponse> csvConsumer) {

    log.info("Sending request to S3");

    var s3ClientBuilder = S3Client.builder();
    if (!StringUtils.isBlank(learningRequest.getRegion()))
      s3ClientBuilder.region(Region.of(learningRequest.getRegion()));

    var s3Client = s3ClientBuilder
        .serviceConfiguration(sc -> sc.useArnRegionEnabled(true))
        .build();

    AlertsReadingResponse response;
    try (
        var responseInputStream = s3Client.getObject(
            GetObjectRequest
                .builder()
                .bucket(learningRequest.getBucket())
                .key(learningRequest.getObject())
                .build())) {

      log.info(
          "Received S3 CSV object with content type = {}",
          responseInputStream.response().contentType());

      response = csvConsumer.apply(
          LearningCsv.fromS3Object(learningRequest.getObject(), responseInputStream));
    } catch (Exception e) {
      log.error(
          "There was a problem when receiving s3 object: ", e);
      throw new AwsS3Exception(e);
    } finally {
      s3Client.close();
    }

    return response;
  }

  private static final class AwsS3Exception extends RuntimeException {

    private static final long serialVersionUID = 3289330223618728867L;

    AwsS3Exception(Exception e) {
      super("There was a problem when receiving s3 object = " + e.getMessage());
    }
  }
}
