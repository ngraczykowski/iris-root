package com.silenteight.payments.bridge.app.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.resource.csv.file.provider.model.*;
import com.silenteight.payments.bridge.common.resource.csv.file.provider.port.CsvFileResourceProvider;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.List;
import javax.annotation.Nonnull;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
public class S3CsvFileResourceProvider implements CsvFileResourceProvider {

  private final S3Client s3Client;
  private final String bucketName;
  private final String prefix;

  @Override
  public Resource getResource(FileRequest fileRequest) {
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
  public void deleteFile(DeleteFileRequest deleteFileRequest) {
    s3Client.deleteObject(DeleteObjectRequest.builder()
        .key(deleteFileRequest.getObject())
        .bucket(deleteFileRequest.getBucket())
        .build());
  }

  public List<FileDetails> getFilesListBasedOnPattern(
      FilesListPatternRequest filesListPatternRequest) {
    try {
      var objectsNames = requestFilesDetails(filesListPatternRequest);
      if (log.isTraceEnabled()) {
        log.trace(
            "Received list of objects in s3 bucket: {},objects: {}",
            filesListPatternRequest.getBucket(), objectsNames);
      }
      return objectsNames;
    } catch (Exception e) {
      log.error("There was a problem when receiving list of objects: ", e);
      throw new S3CsvFileResourceProviderException(e);
    }
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
        .collect(toList());
  }

  private List<FileDetails> requestFilesDetails(FilesListPatternRequest filesListPatternRequest) {
    var requestBuilder = ListObjectsV2Request.builder().bucket(filesListPatternRequest.getBucket());
    if (StringUtils.isNotBlank(filesListPatternRequest.getFilePattern()))
      requestBuilder.prefix(filesListPatternRequest.getFilePattern());

    var response = s3Client.listObjectsV2(requestBuilder.build());

    return createFileDetails(filesListPatternRequest, response);
  }

  @Nonnull
  private List<FileDetails> createFileDetails(
      FilesListPatternRequest filesListPatternRequest, ListObjectsV2Response response) {
    return response.contents()
        .stream()
        .filter(s3object -> s3object.size() > 0)
        .map(s3object -> mapToFileDetail(filesListPatternRequest, s3object))
        .collect(toList());
  }

  private FileDetails mapToFileDetail(
      FilesListPatternRequest filesListPatternRequest, S3Object ro) {
    return FileDetails.builder()
        .bucket(filesListPatternRequest.getBucket())
        .name(ro.key())
        .lastModified(ro.lastModified())
        .build();
  }
}
