package com.silenteight.hsbc.bridge.rest.model.input;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * UsersByGroup
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class UsersByGroup   {
  @JsonProperty("groupId")
  private Integer groupId = null;

  @JsonProperty("groupName")
  private String groupName = null;

  @JsonProperty("userid")
  private Integer userid = null;

  @JsonProperty("username")
  private String username = null;

  @JsonProperty("realm")
  private String realm = null;

  @JsonProperty("fullname")
  private String fullname = null;

  public UsersByGroup groupId(Integer groupId) {
    this.groupId = groupId;
    return this;
  }

  /**
   * Group ID
   * @return groupId
   **/
  @Schema(description = "Group ID")
  
    public Integer getGroupId() {
    return groupId;
  }

  public void setGroupId(Integer groupId) {
    this.groupId = groupId;
  }

  public UsersByGroup groupName(String groupName) {
    this.groupName = groupName;
    return this;
  }

  /**
   * Group Name
   * @return groupName
   **/
  @Schema(description = "Group Name")
  
    public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public UsersByGroup userid(Integer userid) {
    this.userid = userid;
    return this;
  }

  /**
   * Unique ID of the user that is a member of the group
   * @return userid
   **/
  @Schema(description = "Unique ID of the user that is a member of the group")
  
    public Integer getUserid() {
    return userid;
  }

  public void setUserid(Integer userid) {
    this.userid = userid;
  }

  public UsersByGroup username(String username) {
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

  public UsersByGroup realm(String realm) {
    this.realm = realm;
    return this;
  }

  /**
   * source of authentication e.g. domain
   * @return realm
   **/
  @Schema(description = "source of authentication e.g. domain")
  
    public String getRealm() {
    return realm;
  }

  public void setRealm(String realm) {
    this.realm = realm;
  }

  public UsersByGroup fullname(String fullname) {
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
    UsersByGroup usersByGroup = (UsersByGroup) o;
    return Objects.equals(this.groupId, usersByGroup.groupId) &&
        Objects.equals(this.groupName, usersByGroup.groupName) &&
        Objects.equals(this.userid, usersByGroup.userid) &&
        Objects.equals(this.username, usersByGroup.username) &&
        Objects.equals(this.realm, usersByGroup.realm) &&
        Objects.equals(this.fullname, usersByGroup.fullname);
  }

  @Override
  public int hashCode() {
    return Objects.hash(groupId, groupName, userid, username, realm, fullname);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UsersByGroup {\n");
    
    sb.append("    groupId: ").append(toIndentedString(groupId)).append("\n");
    sb.append("    groupName: ").append(toIndentedString(groupName)).append("\n");
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
