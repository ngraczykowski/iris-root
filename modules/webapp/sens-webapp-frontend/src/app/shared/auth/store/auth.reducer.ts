import { Principal } from '@app/shared/auth/principal.model';
import { Action, createReducer, on } from '@ngrx/store';
import AuthActions from './auth.actions';

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
    on(AuthActions.loginSuccess,
        (state, {principal}) => ({
              ...state,
              principal: principal,
              isLoggedIn: true,
              processing: false,
            }
        )),
    on(AuthActions.tryLogin,
        (state) => ({
          ...state,
          error: null,
          processing: true,
        })),
    on(AuthActions.loginFailed,
        (state, {reason}) => ({
          ...state,
          isLoggedIn: false,
          processing: false,
          error: reason
        })),
    on(AuthActions.logout,
        (state) => ({
          ...state,
          processing: true,
          error: false,
        })),
    on(AuthActions.logoutSuccess,
        (state) => ({
          ...state,
          processing: false,
          error: false
        })),
    on(AuthActions.logoutFailed,
        (state, {reason}) => ({
          ...state,
          processing: false,
          error: reason
        }))
);

export default (state: State | undefined, action: Action) => reducer(state, action);

