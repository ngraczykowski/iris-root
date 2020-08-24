import { Authority } from '@core/authorities/model/authority.enum';

export interface UpdateUserRequest {
  displayName: string;
  password?: string;
  roles: Authority[];
  userName: string;
}
