package com.silenteight.hsbc.bridge.aws;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.file.ResourceIdentifier;
import com.silenteight.hsbc.bridge.file.ResourceSaver;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
class AwsAdapter implements ResourceSaver {

  private final S3Client client;
  private final String bucketName;

  @Override
  public ResourceIdentifier save(InputStream file, String fileName) throws IOException {
    var objectResult = putInputStreamObject(file, fileName);
    var uri = createUri(fileName, objectResult.versionId());
    return new ResourceIdentifier(uri);
  }

  private String createUri(String fileName, String versionId) {
    return client.utilities().getUrl(
        builder -> builder
            .bucket(bucketName)
            .key(fileName)
            .versionId(versionId))
        .toExternalForm();
  }

  private PutObjectResponse putInputStreamObject(InputStream file, String fileName)
      throws IOException {
    return client.putObject(
        PutObjectRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .build(),
        RequestBody.fromInputStream(file, file.available())
    );
  }
}
