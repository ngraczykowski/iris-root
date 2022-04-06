package com.silenteight.serp.governance.file.storage;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.filestorage.api.FileRemover;
import com.silenteight.sep.filestorage.api.FileRetriever;
import com.silenteight.sep.filestorage.api.FileUploader;
import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.sep.filestorage.api.dto.StoreFileRequestDto;
import com.silenteight.serp.governance.file.validation.exception.FileBrokenContentException;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.silenteight.serp.governance.file.common.FileResource.fromResourceName;
import static com.silenteight.serp.governance.file.common.FileResource.toResourceName;
import static java.lang.String.format;
import static java.util.UUID.randomUUID;

@Slf4j
@RequiredArgsConstructor
public class FileService {

  @NonNull
  private final String bucket;

  @NonNull
  private final FileRetriever fileRetriever;

  @NonNull
  private final FileUploader fileUploader;

  @NonNull
  private final FileRemover fileRemover;

  public String attemptToSaveFile(MultipartFile file) {
    try {
      return toResourceName(doSaveFile(file));
    } catch (IOException e) {
      log.error("Error occurred during saving process for file {}", file.getOriginalFilename());
      throw new FileBrokenContentException(
          format("Content of file %s is broken", file.getOriginalFilename()), e);
    }
  }

  public byte[] getFile(String fileName) {
    FileDto fileDto = fileRetriever.getFile(bucket, fromResourceName(fileName).toString());
    return getFileContent(fileDto);
  }

  public void deleteFile(String fileName) {
    fileRemover.removeFile(bucket, fromResourceName(fileName).toString());
  }

  private UUID doSaveFile(MultipartFile file) throws IOException {
    UUID fileName = randomUUID();
    log.info("Saving file with name, fileName={}", file.getOriginalFilename());
    StoreFileRequestDto storeFileRequestDto = StoreFileRequestDto
        .builder()
        .storageName(bucket)
        .fileName(fileName.toString())
        .fileSize(file.getSize())
        .fileContent(file.getInputStream())
        .build();

    fileUploader.storeFile(storeFileRequestDto);
    log.debug("File saved, fileName={}", file.getOriginalFilename());
    return fileName;
  }

  private static byte[] getFileContent(FileDto file) {
    try (InputStream content = file.getContent()) {
      return content.readAllBytes();
    } catch (IOException e) {
      throw new FileBrokenContentException(
          format("Content of file with name %s is broken", file.getName()), e);
    }
  }
}
