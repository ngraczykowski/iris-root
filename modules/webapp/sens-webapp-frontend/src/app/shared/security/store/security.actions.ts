import { Principal } from '@app/shared/security/principal.model';
import { Action } from '@ngrx/store';

export enum SecurityActionTypes {
    tryLogin = '[Auth] Try login',
    loginSuccess = '[Auth] Login success',
    setPrincipal = '[Auth] Fetched principal data',
    loginFailed = '[Auth] Login failed',
    logout =  '[Auth] Logout',
    logoutSuccess = '[Auth] Logout success',
    logoutFailed = '[Auth] Logout failed',
    setSessionExpireTime = '[Auth] Set session exp time'
}

export class TryLogin implements Action {
    readonly type = SecurityActionTypes.tryLogin;
}
export class LoginSuccess implements Action {
    readonly type = SecurityActionTypes.loginSuccess;
}
export class SetPrincipal implements Action {
    readonly type = SecurityActionTypes.setPrincipal;

    constructor(public payload: Principal) {}
}
export class LoginFailed implements Action {
    readonly type = SecurityActionTypes.loginFailed;

    constructor(public payload: any) {}
}
export class Logout implements Action {
    readonly type = SecurityActionTypes.logout;
}
export class LogoutSuccess implements Action {
    readonly type = SecurityActionTypes.logoutSuccess;
}
export class LogoutFailed implements Action {
    readonly type = SecurityActionTypes.logoutFailed;

    constructor(public payload: any) {}
}
export class SetSessionExpireTime implements Action {
  readonly type = SecurityActionTypes.setSessionExpireTime;
  constructor(public time: number) {}
}
export type SecurityActions = TryLogin
  | LoginSuccess
  | LoginFailed
  | Logout
  | LogoutSuccess
  | LogoutFailed
  | SetPrincipal
  | SetSessionExpireTime;
