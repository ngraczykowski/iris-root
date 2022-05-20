package com.silenteight.universaldatasource.app.commentinput.port.incoming;

import java.util.List;

public interface DeleteCommentInputsUseCase {

  void delete(List<String> alerts);

}
