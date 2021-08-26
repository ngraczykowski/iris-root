package com.silenteight.sep.filestorage.minio;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.sep.filestorage.api.dto.StoreFileRequestDto;
import com.silenteight.sep.filestorage.minio.manager.MinioStorageManager;
import com.silenteight.sep.filestorage.minio.remove.MinioFileRemover;
import com.silenteight.sep.filestorage.minio.retrieval.MinioFileRetrieval;
import com.silenteight.sep.filestorage.minio.save.MinioFileUploader;

@RequiredArgsConstructor
public class MinioFileService {

  private final MinioFileUploader uploader;

  private final MinioFileRetrieval retrieval;

  private final MinioFileRemover remover;

  private final MinioStorageManager storageManager;

  public void storeFile(StoreFileRequestDto file, String bucketName) {
    uploader.storeFile(file, bucketName);
  }

  public FileDto retrieveFile(String fileName, String bucketName) {
    return retrieval.getFile(fileName, bucketName);
  }

  public void removeFile(String fileName, String bucketName) {
    remover.removeFile(fileName, bucketName);
  }

  public void removeBucket(String bucketName) {
    storageManager.remove(bucketName);
  }

  public void createBucket(String bucketName) {
    storageManager.create(bucketName);
  }

  public boolean bucketExist(String bucketName) {
    return storageManager.storageExist(bucketName);
  }
}
