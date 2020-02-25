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

export interface UserRoles {
  roles: String[];
}
