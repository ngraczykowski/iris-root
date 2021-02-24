package com.silenteight.hsbc.bridge.rest.model.input;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

/**
 * AlertUserInformation
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class AlertUserInformation   {
  @JsonProperty("usersByGroup")
  private UsersByGroup usersByGroup = null;

  @JsonProperty("deletedusers")
  private DeletedUsers deletedusers = null;

  @JsonProperty("users")
  private Users users = null;

  public AlertUserInformation usersByGroup(UsersByGroup usersByGroup) {
    this.usersByGroup = usersByGroup;
    return this;
  }

  /**
   * Get usersByGroup
   * @return usersByGroup
   **/
  @Schema(description = "")
  
    @Valid
    public UsersByGroup getUsersByGroup() {
    return usersByGroup;
  }

  public void setUsersByGroup(UsersByGroup usersByGroup) {
    this.usersByGroup = usersByGroup;
  }

  public AlertUserInformation deletedusers(DeletedUsers deletedusers) {
    this.deletedusers = deletedusers;
    return this;
  }

  /**
   * Get deletedusers
   * @return deletedusers
   **/
  @Schema(description = "")
  
    @Valid
    public DeletedUsers getDeletedusers() {
    return deletedusers;
  }

  public void setDeletedusers(DeletedUsers deletedusers) {
    this.deletedusers = deletedusers;
  }

  public AlertUserInformation users(Users users) {
    this.users = users;
    return this;
  }

  /**
   * Get users
   * @return users
   **/
  @Schema(description = "")
  
    @Valid
    public Users getUsers() {
    return users;
  }

  public void setUsers(Users users) {
    this.users = users;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AlertUserInformation alertUserInformation = (AlertUserInformation) o;
    return Objects.equals(this.usersByGroup, alertUserInformation.usersByGroup) &&
        Objects.equals(this.deletedusers, alertUserInformation.deletedusers) &&
        Objects.equals(this.users, alertUserInformation.users);
  }

  @Override
  public int hashCode() {
    return Objects.hash(usersByGroup, deletedusers, users);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AlertUserInformation {\n");
    
    sb.append("    usersByGroup: ").append(toIndentedString(usersByGroup)).append("\n");
    sb.append("    deletedusers: ").append(toIndentedString(deletedusers)).append("\n");
    sb.append("    users: ").append(toIndentedString(users)).append("\n");
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
