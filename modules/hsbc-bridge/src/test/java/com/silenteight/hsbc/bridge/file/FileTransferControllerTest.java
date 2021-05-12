package com.silenteight.hsbc.bridge.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class FileTransferControllerTest {

  static private final String SUCCESS = "File uploaded successfully";
  static private final String FILE_EXIST_EXCEPTION = "File already exists";
  static private final String NO_FILE_EXCEPTION = "No file was specified";

  FileTransferController underTest;

  @Mock
  private FileTransferUseCase fileTransferUseCase;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    underTest = new FileTransferController(fileTransferUseCase);
  }

  @Test
  public void saveFile_shouldVerifyStatus() throws Exception {
    //given
    var mockMvc = standaloneSetup(underTest).build();
    var file = getMultipartFileMock();

    //then
    mockMvc.perform(multipart("/v1/upload")
        .file(file)
        .accept(MediaType.parseMediaType("multipart/form-data")))
        .andExpect(status().isOk());
  }

  @Test
  public void saveFile_whenFileNameIsEmpty_shouldThrowNoFileException() {
    //given
    var emptyName = " ";
    var mockFile = new MockMultipartFile(emptyName, "some content".getBytes());

    //then
    assertThatThrownBy(() -> underTest.transferFile(mockFile))
        .isInstanceOf(NoFileException.class)
        .hasMessageContaining(NO_FILE_EXCEPTION);
  }

  @Test
  public void saveFile_shouldThrowFileExistsException() {
    //given
    var mockFile = getMultipartFileMock();

    doThrow(new FileExistsException(FILE_EXIST_EXCEPTION))
        .when(fileTransferUseCase)
        .transfer(any(InputStream.class), anyString());

    //then
    assertThatThrownBy(() -> underTest.transferFile(mockFile))
        .isInstanceOf(FileExistsException.class)
        .hasMessageContaining(FILE_EXIST_EXCEPTION);
  }

  private static MockMultipartFile getMultipartFileMock() {
    return new MockMultipartFile(
        "file",
        "someFile.txt",
        MediaType.MULTIPART_FORM_DATA_VALUE,
        "Testing content".getBytes()
    );
  }
}
