package com.silenteight.hsbc.bridge.unpacker;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.io.*;

public class FileUnzipper {

  private static final int BUFFER_SIZE = 1024;

  public UnzippedObject unzip(InputStream compressedFile) {
    try {
      return unzipArchive(compressedFile);
    } catch (IOException e) {
      throw new UnzipFailureException(e);
    }
  }

  private UnzippedObject unzipArchive(InputStream compressedFile) throws IOException {
    var gzipInputStream = new GzipCompressorInputStream(compressedFile);
    var fileOutputStream = new ByteArrayOutputStream();
    writeCompressedFile(gzipInputStream, fileOutputStream);

    var decompressedFile = new ByteArrayInputStream(fileOutputStream.toByteArray());
    return new UnzippedObject(decompressedFile, gzipInputStream.getMetaData().getFilename());
  }

  private void writeCompressedFile(InputStream gzip, OutputStream output) throws IOException {
    byte[] buffer = new byte[BUFFER_SIZE];
    int readBytes;
    while ((readBytes = gzip.read(buffer)) > 0) {
      output.write(buffer, 0, readBytes);
    }
  }
}
