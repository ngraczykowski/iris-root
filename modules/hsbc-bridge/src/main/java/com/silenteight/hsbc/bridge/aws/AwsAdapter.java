package com.silenteight.hsbc.bridge.aws;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.model.transfer.ModelRepository;
import com.silenteight.hsbc.bridge.model.transfer.ModelTransferModelLoader;
import com.silenteight.hsbc.bridge.watchlist.LoadingException;
import com.silenteight.hsbc.bridge.watchlist.WatchlistLoader;
import com.silenteight.hsbc.bridge.watchlist.WatchlistSaver;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static com.silenteight.hsbc.bridge.aws.AwsUriUtils.getObjectKey;

@RequiredArgsConstructor
class AwsAdapter
    implements ModelRepository, WatchlistSaver, WatchlistLoader, ModelTransferModelLoader {

  private final S3Client client;
  private final String modelBucketName;
  private final String watchlistBucketName;

  @Override
  public URI saveModel(String modelUrl, String name) throws IOException {
    putObjectFromInputStream(
        URI.create(modelUrl).toURL().openStream(),
        name,
        modelBucketName);
    var url = createUrl(name, modelBucketName);
    return toUri(url);
  }

  @Override
  public URI save(InputStream inputStream, String name) throws WatchlistSavingException {
    try {
      putObjectFromInputStream(inputStream, name, watchlistBucketName);
      var url = createUrl(name, watchlistBucketName);
      return toUri(url);
    } catch (IOException e) {
      throw new WatchlistSavingException(e);
    }
  }

  @Override
  public URI saveFile(String name, String path) throws WatchlistSavingException {
    try {
      putObjectFromFile(name, watchlistBucketName, path);
      var url = createUrl(name, watchlistBucketName);
      return toUri(url);
    } catch (IOException e) {
      throw new WatchlistSavingException(e);
    }
  }

  @Override
  public ResponseInputStream<GetObjectResponse> loadWatchlist(URI uri) throws LoadingException {
    try {
      return client.getObject(GetObjectRequest.builder()
          .bucket(watchlistBucketName)
          .key(getObjectKey(uri))
          .build());
    } catch (SdkException e) {
      throw new LoadingException(e);
    }
  }

  @Override
  public ResponseInputStream<GetObjectResponse> loadModel(URI uri) throws LoadingException {
    try {
      return client.getObject(GetObjectRequest.builder()
          .bucket(modelBucketName)
          .key(getObjectKey(uri))
          .build());
    } catch (SdkException e) {
      throw new LoadingException(e);
    }
  }

  private PutObjectResponse putObjectFromInputStream(
      InputStream file, String fileName, String bucketName) throws IOException {
    return client.putObject(
        PutObjectRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .build(),
        RequestBody.fromInputStream(file, file.available())
    );
  }

  private PutObjectResponse putObjectFromFile(String fileName, String bucketName, String path) {
    return client.putObject(
        PutObjectRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .build(),
        RequestBody.fromFile(new File(path))
    );
  }

  private URL createUrl(String fileName, String bucketName) {
    return client.utilities().getUrl(
        builder -> builder
            .bucket(bucketName)
            .key(fileName));
  }

  private static URI toUri(URL url) throws IOException {
    try {
      return url.toURI();
    } catch (URISyntaxException e) {
      throw new IOException(e);
    }
  }
}
