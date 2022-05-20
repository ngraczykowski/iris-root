package com.silenteight.hsbc.bridge.watchlist;

import com.silenteight.hsbc.bridge.watchlist.WatchlistController.NoFileException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.InputStream;
import java.net.URI;

import static org.assertj.core.api.Assertions.*;

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
    var mockMvc = MockMvcBuilders.standaloneSetup(underTest).build();
    BDDMockito.given(watchlistSaver.save(ArgumentMatchers.any(InputStream.class), ArgumentMatchers.any(String.class)))
        .willReturn(URI.create("someUri"));

    //then
    mockMvc.perform(MockMvcRequestBuilders.multipart("/watchlist/v1/upload")
            .file(getMultipartFileMock())
            .accept(MediaType.parseMediaType("multipart/form-data")))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void saveFile_whenFileNameIsEmpty_shouldThrowNoFileException() {
    //given
    var emptyName = " ";
    var mockRequest = new MockMultipartHttpServletRequest();
    var mockFile = new MockMultipartFile(emptyName, "some content".getBytes());
    mockRequest.addFile(mockFile);

    //then
    assertThatThrownBy(() -> underTest.transferFile(mockRequest))
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
