package com.silenteight.sep.filestorage.testcontants;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MinioTestCommons {

  public static final String BUCKET_NAME = "test-bucket-name";
  public static final String FILE_NAME = "test_file";
  public static final String FULL_FILE_NAME = "test_file.txt";
  public static final int PART_SIZE = 10485760;
  public static final MockMultipartFile MOCK_MULTIPART_FILE =
      new MockMultipartFile(FILE_NAME, FULL_FILE_NAME, MediaType.TEXT_PLAIN_VALUE,
          "Test Content".getBytes(UTF_8));
}
