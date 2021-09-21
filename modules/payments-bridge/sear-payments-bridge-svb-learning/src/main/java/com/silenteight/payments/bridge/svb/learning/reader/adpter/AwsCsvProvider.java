package com.silenteight.payments.bridge.svb.learning.reader.adpter;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningCsv;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningRequest;
import com.silenteight.payments.bridge.svb.learning.reader.port.CsvFileProvider;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Profile("!mockaws")
class AwsCsvProvider implements CsvFileProvider {

  @Override
  public LearningCsv getLearningCsv(LearningRequest learningRequest) {

    log.info("Sending request to s3");

    AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
        .withRegion(Regions.EU_CENTRAL_1)
        .build();

    var fullObject = s3Client.getObject(
        new GetObjectRequest(learningRequest.getBucket(), learningRequest.getObject()));
    log.info("Received s3 csv object");

    return LearningCsv.fromS3Object(fullObject);
  }
}
