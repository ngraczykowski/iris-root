package com.silenteight.sep.base.common.messaging.compression;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface CompressionBundle {

  Compressor getCompressor();

  Decompressor getDecompressor();

  String getEncoding();

  interface Compressor {

    @NotNull
    OutputStream compress(@NotNull OutputStream outputStream) throws IOException;
  }

  interface Decompressor {

    @NotNull
    InputStream decompress(@NotNull InputStream inputStream) throws IOException;
  }
}
