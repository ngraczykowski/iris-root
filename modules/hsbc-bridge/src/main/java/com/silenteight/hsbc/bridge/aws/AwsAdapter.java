package com.silenteight.hsbc.bridge.aws;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.model.transfer.ModelRepository;
import com.silenteight.hsbc.bridge.watchlist.WatchlistSaver;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@RequiredArgsConstructor
class AwsAdapter implements ModelRepository, WatchlistSaver {

  private final S3Client client;
  private final String modelBucketName;
  private final String watchlistBucketName;

  @Override
  public URI saveModel(String modelUrl, String name) throws IOException {
    var modelInputStream = URI.create(modelUrl).toURL().openStream();

    var objectResult = putInputStreamObject(modelInputStream, name, modelBucketName);
    var url = createUrl(name, objectResult.versionId(), modelBucketName);

    return toUri(url);
  }

  private URL createUrl(String fileName, String versionId, String bucketName) {
    return client.utilities().getUrl(
        builder -> builder
            .bucket(bucketName)
            .key(fileName)
            .versionId(versionId));
  }

  private PutObjectResponse putInputStreamObject(
      InputStream file, String fileName, String bucketName)
      throws IOException {
    return client.putObject(
        PutObjectRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .build(),
        RequestBody.fromInputStream(file, file.available())
    );
  }

  private static URI toUri(URL uri) throws IOException {
    try {
      return uri.toURI();
    } catch (URISyntaxException e) {
      throw new IOException(e);
    }
  }

  @Override
  public URI save(InputStream inputStream, String name) throws WatchlistSavingException {
    try {
      var objectResult = putInputStreamObject(inputStream, name, watchlistBucketName);
      var url = createUrl(name, objectResult.versionId(), watchlistBucketName);

      return toUri(url);
    } catch (IOException e) {
      throw new WatchlistSavingException(e);
    }
  }
}
