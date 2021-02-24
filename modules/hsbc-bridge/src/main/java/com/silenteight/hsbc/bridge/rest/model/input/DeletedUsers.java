package com.silenteight.hsbc.bridge.rest.model.input;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

/**
 * Deletedusers
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class DeletedUsers {
  @JsonProperty("userid")
  private Integer userid = null;

  @JsonProperty("username")
  private String username = null;

  @JsonProperty("display")
  private String display = null;

  @JsonProperty("fullname")
  private String fullname = null;

  public DeletedUsers userid(Integer userid) {
    this.userid = userid;
    return this;
  }

  /**
   * Unique ID of the user
   * @return userid
   **/
  @Schema(description = "Unique ID of the user")
  
    public Integer getUserid() {
    return userid;
  }

  public void setUserid(Integer userid) {
    this.userid = userid;
  }

  public DeletedUsers username(String username) {
    this.username = username;
    return this;
  }

  /**
   * The users username
   * @return username
   **/
  @Schema(description = "The users username")
  
    public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public DeletedUsers display(String display) {
    this.display = display;
    return this;
  }

  /**
   * The name that will be used to display the user against their activities once the end user has been deleted from the solution.
   * @return display
   **/
  @Schema(description = "The name that will be used to display the user against their activities once the end user has been deleted from the solution.")
  
    public String getDisplay() {
    return display;
  }

  public void setDisplay(String display) {
    this.display = display;
  }

  public DeletedUsers fullname(String fullname) {
    this.fullname = fullname;
    return this;
  }

  /**
   * Users full name
   * @return fullname
   **/
  @Schema(description = "Users full name")
  
    public String getFullname() {
    return fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeletedUsers deletedusers = (DeletedUsers) o;
    return Objects.equals(this.userid, deletedusers.userid) &&
        Objects.equals(this.username, deletedusers.username) &&
        Objects.equals(this.display, deletedusers.display) &&
        Objects.equals(this.fullname, deletedusers.fullname);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userid, username, display, fullname);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Deletedusers {\n");
    
    sb.append("    userid: ").append(toIndentedString(userid)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    display: ").append(toIndentedString(display)).append("\n");
    sb.append("    fullname: ").append(toIndentedString(fullname)).append("\n");
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
