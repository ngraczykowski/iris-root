package com.silenteight.payments.bridge.app.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.resource.csv.file.provider.model.DeleteLearningFileRequest;
import com.silenteight.payments.bridge.common.resource.csv.file.provider.model.FileRequest;
import com.silenteight.payments.bridge.common.resource.csv.file.provider.model.ObjectPath;
import com.silenteight.payments.bridge.common.resource.csv.file.provider.port.CsvFileResourceProvider;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
public class S3CsvFileResourceProvider implements CsvFileResourceProvider {

  private final S3Client s3Client;
  private final String bucketName;
  private final String prefix;


  @Override
  public Resource getResource(
      FileRequest fileRequest) {
    var responseInputStream = s3Client.getObject(
        GetObjectRequest
            .builder()
            .bucket(fileRequest.getBucket())
            .key(fileRequest.getObject())
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

  @Override
  public List<ObjectPath> getFilesList() {
    try {
      var objectsNames = requestFilesNames();
      if (log.isTraceEnabled()) {
        log.trace(
            "Received list of objects in s3 bucket: {},objects: {}", bucketName, objectsNames);
      }
      return objectsNames
          .stream()
          .map(ob -> ObjectPath
              .builder()
              .name(ob)
              .bucket(bucketName)
              .build())
          .collect(toList());
    } catch (Exception e) {
      log.error("There was a problem when receiving list of objects: ", e);
      throw new S3CsvFileResourceProviderException(e);
    }
  }

  private List<String> requestFilesNames() {
    var requestBuilder = ListObjectsV2Request.builder().bucket(bucketName);
    if (StringUtils.isNotBlank(prefix))
      requestBuilder.prefix(prefix);

    var response = s3Client.listObjectsV2(requestBuilder.build());

    return response.contents()
        .stream()
        .filter(o -> o.size() > 0) // Need to filter folders cause aws treats them as file as well.
        .map(S3Object::key)
        .collect(
            toList());
  }

}
