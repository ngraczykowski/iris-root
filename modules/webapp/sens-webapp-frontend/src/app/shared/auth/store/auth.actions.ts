import { Principal } from '@app/shared/auth/principal.model';
import { createAction, props } from '@ngrx/store';

export const tryLogin = createAction(
    '[Auth] Try login',
);

export const loginSuccess = createAction(
    '[Auth] Login success',
    props<{ principal: Principal }>(),
);

export const loginFailed = createAction(
    '[Auth] Login failed',
    props<{ reason }>(),
);

export const logout = createAction(
    '[Auth] Logout'
);

export const logoutSuccess = createAction(
    '[Auth] Logout success'
);

export const logoutFailed = createAction(
    '[Auth] Logout failed',
    props<{ reason }>()
);

export default {
  tryLogin,
  loginSuccess,
  loginFailed,
  logout,
  logoutSuccess,
  logoutFailed
};
