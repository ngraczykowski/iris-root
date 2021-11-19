package com.silenteight.payments.bridge.svb.newlearning.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.domain.ObjectPath;
import com.silenteight.payments.bridge.svb.newlearning.port.FileListPort;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@Profile("!mockaws")
@EnableConfigurationProperties(AwsFileListProviderProperties.class)
@RequiredArgsConstructor
class AwsFileListProvider implements FileListPort {

  private final AwsFileListProviderProperties properties;

  @Override
  public List<ObjectPath> getFilesList() {

    log.info("Receiving list of objects in S3 bucket");

    var s3Client = S3Client.builder()
        .serviceConfiguration(sc -> sc.useArnRegionEnabled(true))
        .build();

    List<String> objectsNames;
    try {
      objectsNames = requestFilesNames(s3Client);
      log.info("Received list of objects in s3 bucket = {}", objectsNames);
    } catch (Exception e) {
      log.error("There was a problem when receiving list of objects: ", e);
      throw new AwsS3Exception(e);
    } finally {
      s3Client.close();
    }

    return objectsNames
        .stream()
        .map(ob -> ObjectPath
            .builder()
            .name(ob)
            .bucket(properties.getBucketName())
            .build())
        .collect(toList());
  }

  private List<String> requestFilesNames(S3Client s3Client) {
    var response = s3Client.listObjectsV2(
        ListObjectsV2Request.builder()
            .bucket(properties.getBucketName())
            .build());

    return response.contents()
        .stream()
        .filter(o -> o.size() > 0) // Need to filter folders cause aws treats them as file as well
        .map(S3Object::key)
        .collect(
            toList());
  }

  private static final class AwsS3Exception extends RuntimeException {

    private static final long serialVersionUID = 3289330223618728867L;

    AwsS3Exception(Exception e) {
      super("There was a problem when receiving list of objects = " + e.getMessage());
    }
  }
}
