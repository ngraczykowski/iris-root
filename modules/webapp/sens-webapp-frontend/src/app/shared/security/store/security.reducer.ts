import { Principal } from '@app/shared/security/principal.model';
import { Action, createReducer, on } from '@ngrx/store';
import SecurityActions from './security.actions';

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

const reducer = createReducer(
    initialState,
    on(SecurityActions.loginSuccess,
        (state) => ({
              ...state,
              isLoggedIn: true,
              processing: false,
            }
        )),
    on(SecurityActions.tryLogin,
        (state) => ({
          ...state,
          error: null,
          processing: true,
        })),
    on(SecurityActions.loginFailed,
        (state, {reason}) => ({
          ...state,
          isLoggedIn: false,
          processing: false,
          error: reason
        })),
    on(SecurityActions.logout,
        (state) => ({
          ...state,
          processing: true,
          error: false,
        })),
    on(SecurityActions.logoutSuccess,
        (state) => ({
          ...state,
          processing: false,
          error: false,
          principal: null,
          isLoggedIn: false
        })),
    on(SecurityActions.logoutFailed,
        (state, {reason}) => ({
          ...state,
          processing: false,
          error: reason
        })),
    on(SecurityActions.setPrincipal,
        (state, {principal}) => ({
          ...state,
          principal: principal
        }))
);

export default (state: State | undefined, action: Action) => reducer(state, action);

