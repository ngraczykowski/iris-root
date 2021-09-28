package com.silenteight.serp.governance.file.storage;

import lombok.SneakyThrows;

import com.silenteight.sep.filestorage.api.FileRemover;
import com.silenteight.sep.filestorage.api.FileRetriever;
import com.silenteight.sep.filestorage.api.FileUploader;
import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.sep.filestorage.api.dto.StoreFileRequestDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

import static com.silenteight.serp.governance.file.common.FileResource.fromResourceName;
import static com.silenteight.serp.governance.file.common.FileResource.toResourceName;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.TEXT_XML_VALUE;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

  private static final String TEST_FILE_CONTENT = "Test file content";
  private static final InputStream FILE_CONTENT_AS_STREAM =
      new ByteArrayInputStream(TEST_FILE_CONTENT.getBytes());

  private static final String TEST_BUCKET = "test_bucket";
  private static final UUID FILE_ID = randomUUID();
  private static final String FILE_NAME = toResourceName(FILE_ID);
  private static final FileDto FILE_DTO = FileDto.builder()
      .name("test.pdf")
      .content(FILE_CONTENT_AS_STREAM)
      .sizeInBytes(12345)
      .build();

  private static final MockMultipartFile MOCK_MULTIPART_FILE =
      new MockMultipartFile("test", "test.pdf",
          TEXT_XML_VALUE,
          TEST_FILE_CONTENT.getBytes(UTF_8));

  @Mock
  FileRetriever fileRetriever;

  @Mock
  FileUploader fileUploader;

  @Mock
  FileRemover fileRemover;

  @Autowired
  FileService underTest;

  @BeforeEach
  void setUp() {
    underTest =
        new FileService(TEST_BUCKET, fileRetriever, fileUploader, fileRemover);
  }

  @Test
  @SneakyThrows
  void shouldReturnFileWrapper() {
    //given
    byte[] expectedBytes = TEST_FILE_CONTENT.getBytes();

    //when
    when(fileRetriever.getFile(TEST_BUCKET, FILE_ID.toString())).thenReturn(FILE_DTO);

    byte[] fileAsBytes = underTest.getFile(FILE_NAME);

    //then
    assertThat(fileAsBytes).hasSameSizeAs(expectedBytes);
  }

  @Test
  @SneakyThrows
  void shouldCreateRequestForSaveFile() {
    //given
    ArgumentCaptor<StoreFileRequestDto> storeFileRequestDtoCaptor =
        forClass(StoreFileRequestDto.class);

    //when
    String savedFileName = underTest.attemptToSaveFile(MOCK_MULTIPART_FILE);

    //then
    verify(fileUploader, times(1)).storeFile(storeFileRequestDtoCaptor.capture());
    assertThat(storeFileRequestDtoCaptor.getValue().getFileSize())
        .isEqualTo(MOCK_MULTIPART_FILE.getSize());

    assertThat(storeFileRequestDtoCaptor.getValue().getFileName())
        .isEqualTo(fromResourceName(savedFileName).toString());

    assertThat(storeFileRequestDtoCaptor.getValue().getFileContent().readAllBytes())
        .isEqualTo(MOCK_MULTIPART_FILE.getInputStream().readAllBytes());
  }
}
