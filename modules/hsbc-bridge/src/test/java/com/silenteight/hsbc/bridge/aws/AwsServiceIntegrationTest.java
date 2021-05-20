package com.silenteight.hsbc.bridge.aws;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;

import static org.assertj.core.api.Assertions.*;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@Testcontainers
class AwsServiceIntegrationTest {

  private static final String BUCKET_NAME = "test-bucket";
  private static final String KEY_NAME = "file";
  private static final String FILE_CONTENT = "someContent";

  DockerImageName localstackImage = DockerImageName.parse("localstack/localstack");
  private AwsAdapter awsAdapter;
  private S3Client s3client;

  @Container
  LocalStackContainer localstack = new LocalStackContainer(localstackImage)
      .withServices(S3);

  @BeforeEach
  public void setup() {
    s3client = S3Client
        .builder()
        .endpointOverride(localstack.getEndpointOverride(LocalStackContainer.Service.S3))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(localstack.getAccessKey(), localstack.getSecretKey())))
        .region(Region.of(localstack.getRegion()))
        .build();

    s3client.createBucket(b -> b.bucket(BUCKET_NAME));
    awsAdapter = new AwsAdapter(s3client, BUCKET_NAME);
  }

  @Test
  public void transferFile_shouldTransferFileToServer() throws IOException {
    //given
    var content = FILE_CONTENT.getBytes();
    var file = new ByteArrayInputStream(content);

    //when
    awsAdapter.save(file, KEY_NAME);

    //then
    var object = s3client.getObject(
        prepareResponse(BUCKET_NAME, KEY_NAME),
        ResponseTransformer.toInputStream());

    assertThat(object.readAllBytes()).isEqualTo(content);
  }

  private static GetObjectRequest prepareResponse(String bucketName, String keyName) {
    return GetObjectRequest.builder()
        .bucket(bucketName)
        .key(keyName)
        .build();
  }
}