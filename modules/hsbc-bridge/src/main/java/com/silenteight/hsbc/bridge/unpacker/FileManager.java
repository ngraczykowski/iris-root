package com.silenteight.hsbc.bridge.unpacker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.deleteIfExists;
import static java.nio.file.Files.exists;
import static java.nio.file.Paths.get;
import static org.apache.commons.io.IOUtils.copyLarge;

@Slf4j
@RequiredArgsConstructor
public class FileManager {

  private final String path;

  public UnzippedObject unzip(InputStream compressedFile) {
    try {
      createDirectory();
      return unzipArchive(compressedFile);
    } catch (IOException e) {
      log.info("Error occurred during unzipping.", e);
      throw new UnzipFailureException(e);
    }
  }

  public boolean delete(String filePath) {
    try {
      return deleteArchive(filePath);
    } catch (IOException e) {
      log.error("Error during deleting file from path: " + filePath, e);
      throw new DeleteFailureException(
          "Unable to delete file from path: " + filePath + " with message: " + e.getMessage(), e);
    }
  }

  private boolean deleteArchive(String filePath) throws IOException {
    var result = deleteIfExists(get(filePath));
    if (result) {
      log.info("File successfully deleted from path: " + filePath);
    } else {
      log.info("Unable to delete the file from path: " + filePath);
    }
    return result;
  }

  private UnzippedObject unzipArchive(InputStream compressedFile) throws IOException {
    var gzipInputStream = new GzipCompressorInputStream(compressedFile);
    var name = gzipInputStream.getMetaData().getFilename();
    var filePath = createPath(name);

    var fileOutputStream = new FileOutputStream(filePath);
    copyLarge(gzipInputStream, fileOutputStream);
    fileOutputStream.close();

    log.info("File successfully unzipped to: " + filePath);
    return new UnzippedObject(name, filePath);
  }

  private void createDirectory() throws IOException {
    var directoryPath = get(path);
    if (exists(directoryPath)) {
      log.info("Directory already exists on path: " + directoryPath);
    } else {
      log.info("Create new directory on path: " + directoryPath);
      createDirectories(directoryPath);
    }
  }

  private String createPath(String name) {
    return path + File.separator + name;
  }
}
