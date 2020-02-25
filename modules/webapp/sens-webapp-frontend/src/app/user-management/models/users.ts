export interface User {
  userName: String;
  displayName: String;
  roles: String[];
  lastLoginAt: String;
  createdAt: String;
  active: boolean;
}

export interface UserResponse {
  content: User[];
}

export interface UserRolesResponse {
  roles: String[];
}

export interface UserRoles {
  roles: UserRoleEntity[];
}

export interface UserRoleEntity {
  role: String;
  label: String;
}
