import { UserRole } from '../../../model/user.model';

export interface NewUserTemplate {
  userName: string;
  displayName?: string;
  password?: string;
  roles: UserRole[];
  superUser?: boolean;
}
