import { Principal } from '@app/shared/security/principal.model';

export const SECURITY_STATE_NAME = 'security';

export interface SecurityState {
  principal: Principal;
  isLoggedIn: boolean;
  processing: boolean;
  sessionExpireTime: number;
  error: any;
}

export const initialState: SecurityState = {
  principal: null,
  isLoggedIn: false,
  processing: false,
  sessionExpireTime: 0,
  error: null
};

export interface StateWithSecurity {
  [SECURITY_STATE_NAME]: SecurityState;
}
