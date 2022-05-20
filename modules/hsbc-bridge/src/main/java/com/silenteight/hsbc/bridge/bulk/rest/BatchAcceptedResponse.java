/*
 * HSCB-Bridge-batch API
 * HSCB-Bridge-batch API
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.silenteight.hsbc.bridge.bulk.rest;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;
/**
 * BatchAcceptedResponse
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-04-02T11:57:31.043Z[GMT]")
public class BatchAcceptedResponse {
  @SerializedName("batchId")
  private String batchId = null;

  public BatchAcceptedResponse batchId(String batchId) {
    this.batchId = batchId;
    return this;
  }

   /**
   * Get batchId
   * @return batchId
  **/
  @Schema(description = "")
  public String getBatchId() {
    return batchId;
  }

  public void setBatchId(String batchId) {
    this.batchId = batchId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BatchAcceptedResponse batchAcceptedResponse = (BatchAcceptedResponse) o;
    return Objects.equals(this.batchId, batchAcceptedResponse.batchId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(batchId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BatchAcceptedResponse {\n");
    
    sb.append("    batchId: ").append(toIndentedString(batchId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
