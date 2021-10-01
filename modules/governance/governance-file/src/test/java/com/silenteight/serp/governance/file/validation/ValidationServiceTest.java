package com.silenteight.serp.governance.file.validation;

import lombok.SneakyThrows;

import com.silenteight.serp.governance.file.validation.exception.InvalidFileException;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.http.MediaType.TEXT_XML_VALUE;

class ValidationServiceTest {

  private ValidationService underTest;

  private static final MockMultipartFile MOCK_MULTIPART_FILE_TXT =
      new MockMultipartFile("test_file", "test()_-#.txt", TEXT_PLAIN_VALUE,
          getTestFileAsBytes("src/test/resources/test-files/test()_-#.txt"));

  private static final MockMultipartFile MOCK_MULTIPART_FILE_PNG =
      new MockMultipartFile("test_file_2", "test_file.pdf", APPLICATION_PDF_VALUE,
          getTestFileAsBytes("src/test/resources/test-files/test.png"));

  private static final MockMultipartFile MOCK_MULTIPART_FILE_WITH_BIG_SIZE_AND_WRONG_TYPE =
      new MockMultipartFile("test_file_3", "test_file.cbor", APPLICATION_OCTET_STREAM_VALUE,
          new byte[21000000]);

  private static final MockMultipartFile MOCK_MULTIPART_FILE_CSV =
      new MockMultipartFile("test_file._4",
          "AI Reasoning last 24h_2021-09-17T10_10_50.943Z_891f94f0-179f-52ec-a00e-6b25ef5112c0.csv",
          TEXT_XML_VALUE, getTestFileAsBytes(
          "src/test/resources/test-files/AI Reasoning last "
              + "24h_2021-09-17T10_10_50.943Z_891f94f0-179f-52ec-a00e-6b25ef5112c0.csv"));

  private static final MockMultipartFile MOCK_MULTIPART_FILE_WITH_BIG_FILE_NAME =
      new MockMultipartFile("test_file_5", createFileNMameWithOver255Chars(),
          APPLICATION_OCTET_STREAM_VALUE,
          "Test Content".getBytes(UTF_8));

  private static final MockMultipartFile MOCK_MULTIPART_FILE_WITH_WRONG_NAME =
      new MockMultipartFile("test&#$#@(!_file_&%#", "test&#$#@(!_file_&%#.xml",
          TEXT_XML_VALUE,
          "Test Content".getBytes(UTF_8));

  private static final MockMultipartFile MOCK_MULTIPART_FILE_WRONG_TYPE =
      new MockMultipartFile("test-zip", "test.zip", APPLICATION_OCTET_STREAM_VALUE,
          getTestFileAsBytes("src/test/resources/test-files/test.zip"));

  @BeforeEach
  public void init() {
    ValidationProperties properties = getValidationProperties();

    FileValidator nameCharactersValidator =
        new FileNameCharactersValidator(properties.getAllowedCharactersForFileName());

    FileValidator sizeValidator = new FileSizeValidator(properties.getMaxFileSizeInBytes());
    FileValidator nameLengthValidator =
        new FileNameLengthValidator(properties.getMaxFileNameLength());

    FileValidator mimeTypeValidator =
        new FileMimeTypeValidator(properties.allowedTypes);

    underTest = new ValidationService(
        nameCharactersValidator, mimeTypeValidator, nameLengthValidator, sizeValidator);
  }

  @ParameterizedTest
  @MethodSource("getNotAllowedAttachmentsList")
  void shouldThrowExceptionWhenFileIsNotValid(
      MockMultipartFile file) {

    assertThrows(InvalidFileException.class, () -> underTest.validate(file));

  }

  @ParameterizedTest
  @MethodSource("getAllowedAttachmentsList")
  @SneakyThrows
  void shouldValidateAllowedAttachments(MockMultipartFile file) {
    assertDoesNotThrow(() -> underTest.validate(file));
  }

  private static String createFileNMameWithOver255Chars() {
    return StringUtils.repeat("abc", 86);
  }

  @NotNull
  private ValidationProperties getValidationProperties() {
    ValidationProperties properties = new ValidationProperties();
    properties.setMaxFileNameLength(255);
    properties.setMaxFileSizeInBytes(20971520L);
    properties.setMaxNumberFilesToUpload(20);
    properties.setAllowedTypes(
        of("image/jpeg", "image/png", "image/gif", "image/svg+xml", "application/pdf",
            "application/x-tika-msoffice", "application/x-tika-ooxml", "application/x-tika-ooxml",
            "application/x-tika-msoffice", "text/plain", "text/plain", "text/plain",
            "application/x-tika-msoffice"));
    properties.setAllowedCharactersForFileName("^[.a-zA-Z0-9-()#_\\s]*$");
    return properties;
  }

  private static Stream<Arguments> getNotAllowedAttachmentsList() {
    return Stream.of(
        Arguments.of(MOCK_MULTIPART_FILE_WRONG_TYPE),
        Arguments.of(MOCK_MULTIPART_FILE_WITH_BIG_SIZE_AND_WRONG_TYPE),
        Arguments.of(MOCK_MULTIPART_FILE_WITH_BIG_FILE_NAME),
        Arguments.of(MOCK_MULTIPART_FILE_WITH_WRONG_NAME)
    );
  }

  private static Stream<Arguments> getAllowedAttachmentsList() {
    return Stream.of(
        Arguments.of(MOCK_MULTIPART_FILE_TXT),
        Arguments.of(MOCK_MULTIPART_FILE_PNG),
        Arguments.of(MOCK_MULTIPART_FILE_CSV)
    );
  }

  @SneakyThrows
  private static byte[] getTestFileAsBytes(String pathForFile) {
    Path filePath = get(pathForFile);
    return readAllBytes(filePath);
  }
}
