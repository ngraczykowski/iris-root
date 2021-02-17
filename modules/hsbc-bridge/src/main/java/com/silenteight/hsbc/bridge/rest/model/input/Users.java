package com.silenteight.hsbc.bridge.rest.model.input;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Users
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class Users   {
  @JsonProperty("userid")
  private Integer userid = null;

  @JsonProperty("username")
  private String username = null;

  @JsonProperty("realm")
  private String realm = null;

  @JsonProperty("fullname")
  private String fullname = null;

  public Users userid(Integer userid) {
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

  public Users username(String username) {
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

  public Users realm(String realm) {
    this.realm = realm;
    return this;
  }

  /**
   * Source of authentication e.g. domain
   * @return realm
   **/
  @Schema(description = "Source of authentication e.g. domain")
  
    public String getRealm() {
    return realm;
  }

  public void setRealm(String realm) {
    this.realm = realm;
  }

  public Users fullname(String fullname) {
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
    Users users = (Users) o;
    return Objects.equals(this.userid, users.userid) &&
        Objects.equals(this.username, users.username) &&
        Objects.equals(this.realm, users.realm) &&
        Objects.equals(this.fullname, users.fullname);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userid, username, realm, fullname);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Users {\n");
    
    sb.append("    userid: ").append(toIndentedString(userid)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    realm: ").append(toIndentedString(realm)).append("\n");
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
