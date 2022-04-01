package com.silenteight.connector.ftcc.common.dto.output;

import lombok.Getter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Getter
@ToString
class AckBodyDto implements Serializable {

  private static final long serialVersionUID = 8305008907553975177L;

  @JsonProperty("msg_Acknowledgement")
  private final AckMessageDto messageDto;

  //You may wonder why @JsonCreator is needed here, while not needed in AckMessageDto and AckDto
  //Explanation: https://github.com/FasterXML/jackson-databind/issues/1498
  @JsonCreator
  public AckBodyDto(AckMessageDto messageDto) {
    this.messageDto = messageDto;
  }

}
