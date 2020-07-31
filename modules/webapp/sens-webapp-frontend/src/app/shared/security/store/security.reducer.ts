import { initialState, SecurityState } from '@app/shared/security/store/security.state';
import { SecurityActions, SecurityActionTypes } from './security.actions';

export function reducer(state = initialState, action: SecurityActions): SecurityState {
  switch (action.type) {
    case SecurityActionTypes.loginSuccess:
      return {
        ...state,
        isLoggedIn: true,
        processing: false,
      };

    case SecurityActionTypes.tryLogin:
      return {
        ...state,
        error: null,
        processing: true,
      };

    case SecurityActionTypes.loginFailed:
      return {
        ...state,
        isLoggedIn: false,
        processing: false,
        error: action.payload.reason
      };

    case SecurityActionTypes.logout:
      return {
        ...state,
        processing: true,
        error: false,
      };

    case SecurityActionTypes.logoutSuccess:
      return {
        ...state,
        processing: false,
        error: false,
        principal: null,
        isLoggedIn: false
      };

    case SecurityActionTypes.logoutFailed:
      return {
        ...state,
        processing: false,
        error: action.payload.reason
      };

    case SecurityActionTypes.setPrincipal:
      return {
        ...state,
        principal: action.payload
      };
    case SecurityActionTypes.setSessionExpireTime:
      return {
        ...state,
        sessionExpireTime: action.time
      };
  }
}
