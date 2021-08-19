package com.silenteight.hsbc.bridge.watchlist;

import com.silenteight.hsbc.bridge.watchlist.WatchlistController.NoFileException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStream;
import java.net.URI;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class WatchlistControllerTest {

  private static final String NO_FILE_EXCEPTION = "No file was specified";
  private static final String ZIP_FILE = "file";

  WatchlistController underTest;

  @Mock
  private WatchlistSaver watchlistSaver;
  @Mock
  private ApplicationEventPublisher eventPublisher;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    underTest = new WatchlistController(watchlistSaver, eventPublisher);
  }

  @Test
  public void saveFile_shouldVerifyStatus() throws Exception {
    //given
    var mockMvc = standaloneSetup(underTest).build();
    given(watchlistSaver.save(any(InputStream.class), any(String.class)))
        .willReturn(URI.create("someUri"));

    //then
    mockMvc.perform(multipart("/watchlist/v1/upload")
        .file(getMultipartFileMock())
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
        ZIP_FILE,
        "someFile.zip",
        MediaType.MULTIPART_FORM_DATA_VALUE,
        "Testing content".getBytes()
    );
  }
}
