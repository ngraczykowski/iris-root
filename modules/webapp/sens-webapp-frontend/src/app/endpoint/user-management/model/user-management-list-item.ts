export interface UserManagementListItem {
  userName: string;
  displayName: string;
  roles: string[];
  lastLoginAt: string;
  createdAt: string;
  origin: string;
  active: boolean;
}
