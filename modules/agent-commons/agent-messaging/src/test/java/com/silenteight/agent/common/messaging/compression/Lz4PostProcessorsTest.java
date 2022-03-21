package com.silenteight.agent.common.messaging.compression;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.*;

class Lz4PostProcessorsTest {

  private static final int COMPRESSION_LEVEL = 4;

  private final Lz4CompressingPostProcessor compressingPostProcessor =
      new Lz4CompressingPostProcessor(COMPRESSION_LEVEL);

  private final Lz4DecompressingPostProcessor decompressingPostProcessor =
      new Lz4DecompressingPostProcessor();

  @Test
  void compressesAndDecompressesCorrectly() throws IOException {
    String expected = "messageToCompress";

    var compressedStream = new ByteArrayOutputStream();
    var compressingStream = compressingPostProcessor.getCompressorStream(compressedStream);

    compressingStream.write(expected.getBytes(UTF_8));
    compressingStream.close();

    var inputStream = new ByteArrayInputStream(compressedStream.toByteArray());
    var decompressingStream = decompressingPostProcessor.getDecompressorStream(inputStream);

    var actual = new String(decompressingStream.readAllBytes(), UTF_8);
    assertThat(actual).isEqualTo(expected);
  }
}
