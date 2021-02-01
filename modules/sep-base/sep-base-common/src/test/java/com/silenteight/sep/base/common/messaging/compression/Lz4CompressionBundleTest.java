package com.silenteight.sep.base.common.messaging.compression;

import com.silenteight.sep.base.common.messaging.compression.CompressionBundle.Compressor;
import com.silenteight.sep.base.common.messaging.compression.CompressionBundle.Decompressor;

import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.*;

class Lz4CompressionBundleTest {

  private final CompressionBundle underTest = new Lz4CompressionBundleConfigurer(4).configure();
  private final Compressor compressor = underTest.getCompressor();
  private final Decompressor decompressor = underTest.getDecompressor();

  @Test
  void compressesAndDecompressesCorrectly() throws IOException {
    String expected = "messageToCompress";

    var compressedStream = new ByteArrayOutputStream();
    var compressingStream = compressor.compress(compressedStream);

    compressingStream.write(expected.getBytes(UTF_8));
    compressingStream.close();

    var decompressingStream = decompressor.decompress(compressedStream.toInputStream());

    var actual = new String(decompressingStream.readAllBytes(), UTF_8);

    assertThat(actual).isEqualTo(expected);
  }
}
