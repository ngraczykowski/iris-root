import { Principal } from '@app/shared/security/principal.model';
import { Action, createReducer, on } from '@ngrx/store';
import { SecurityActions, SecurityActionTypes } from './security.actions';

export interface State {
  principal: Principal;
  isLoggedIn: boolean;
  processing: boolean;
  error: any;
}

export const initialState: State = {
  principal: null,
  isLoggedIn: false,
  processing: false,
  error: null
};


export function reducer(state = initialState, action: SecurityActions): State {
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
  }
}
