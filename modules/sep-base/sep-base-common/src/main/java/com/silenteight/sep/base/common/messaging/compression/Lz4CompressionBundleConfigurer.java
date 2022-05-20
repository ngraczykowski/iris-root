package com.silenteight.sep.base.common.messaging.compression;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Lz4CompressionBundleConfigurer {
  public static final int FASTEST_COMPRESSION = 1;
  public static final int BEST_COMPRESSION = 9;
  private final int compressionLevel;

  public CompressionBundle configure() {
    var compressor = new Lz4Compressor(compressionLevel);
    var decompressor = new Lz4Decompressor();

    return new SimpleCompressionBundle(compressor, decompressor, "lz4");
  }
}
