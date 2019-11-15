import { UserRole } from '../../../model/user.model';

export interface EditUserTemplate {
  password?: string;
  displayName?: string;
  roles?: UserRole[];
  active?: boolean;
  superUser?: boolean;
}
