package com.silenteight.hsbc.bridge.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class WatchlistControllerTest {

  static private final String NO_FILE_EXCEPTION = "No file was specified";

  WatchlistController underTest;

  @Mock
  private SaveFileUseCase saveFileUseCase;
  @Mock
  private WorldCheckNotifierServiceClient worldCheckNotifierServiceClient;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    underTest = new WatchlistController(saveFileUseCase, worldCheckNotifierServiceClient);
  }

  @Test
  public void saveFile_shouldVerifyStatus() throws Exception {
    //given
    var mockMvc = standaloneSetup(underTest).build();
    var file = getMultipartFileMock();

    //then
    mockMvc.perform(multipart("/watchlist/v1/upload")
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

  private static MockMultipartFile getMultipartFileMock() {
    return new MockMultipartFile(
        "file",
        "someFile.txt",
        MediaType.MULTIPART_FORM_DATA_VALUE,
        "Testing content".getBytes()
    );
  }
}
