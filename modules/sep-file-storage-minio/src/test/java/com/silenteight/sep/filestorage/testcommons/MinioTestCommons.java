package com.silenteight.sep.filestorage.testcommons;

import org.springframework.mock.web.MockMultipartFile;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

public class MinioTestCommons {

  public static final String BUCKET_NAME = "test-bucket-name";
  public static final String FILE_NAME = "test_file";
  public static final String FILE_NAME_2 = "test_file_2";
  public static final String FULL_FILE_NAME = "test_file.txt";
  public static final String FULL_FILE_NAME_2 = "contract.pdf";
  public static final int PART_SIZE = 10485760;
  public static final MockMultipartFile MOCK_MULTIPART_FILE_TXT =
      new MockMultipartFile(FILE_NAME, FULL_FILE_NAME, TEXT_PLAIN_VALUE,
          "Test Content".getBytes(UTF_8));

  public static final MockMultipartFile MOCK_MULTIPART_FILE_PDF =
      new MockMultipartFile(FILE_NAME_2, FULL_FILE_NAME_2, APPLICATION_PDF_VALUE,
          "<<pdf data>>".getBytes(UTF_8));
}
