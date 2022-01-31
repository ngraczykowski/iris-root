package com.silenteight.payments.bridge.common.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.Nullable;

import static org.apache.commons.lang3.exception.ExceptionUtils.rethrow;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileProcessor {

  public static File createZipFile(File file) throws IOException {

    File zipFile = createTempFile("zipFile", null);

    try (
        ZipOutputStream zipFileOutput = new ZipOutputStream(new FileOutputStream(zipFile));
        InputStream input = new FileInputStream(file)) {

      zipFileOutput.putNextEntry(new ZipEntry(file.getName()));
      int temp = 0;
      while ((temp = input.read()) != -1) {
        zipFileOutput.write(temp);
      }

    } catch (IOException exception) {
      log.error(
          "Error during creating zip file. Message= {}, reason= {}.",
          exception.getMessage(), exception.getCause());
      return rethrow(exception);
    }

    return zipFile;
  }

  public static File createTempFile(String name, @Nullable String extension) {
    try {
      return File.createTempFile(name, extension);
    } catch (IOException exception) {
      log.error(
          "Error during creating temp file. Message= {}, reason= {}.",
          exception.getMessage(), exception.getCause());
      return rethrow(exception);
    }
  }

  public static void deleteFile(@Nullable File file) {
    Optional.ofNullable(file).ifPresent(f -> {
      try {
        Files.deleteIfExists(f.toPath());
      } catch (IOException exception) {
        log.error(
            "Error during deleting temp file, path={}. Message= {}, reason= {}.",
            f.toPath(), exception.getMessage(), exception.getCause());
      }
    });
  }
}
