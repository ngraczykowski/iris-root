import { UserManagementActions, UserManagementActionTypes } from '../actions/userManagement.actions';
import { createSelector } from '@ngrx/store';

export interface State {
  usersTable: any;
  usersDetails: any;
  loaded: boolean;
  loadingError: boolean;
}

export const initialState: State = {
  usersTable: [],
  usersDetails: [],
  loaded: false,
  loadingError: false
};

export function reducer(state = initialState, action: UserManagementActions): State {
  switch (action.type) {

    case UserManagementActionTypes.LoadUsersSuccess:
      return {
        ...state,
        usersTable: action.payload,
        loaded: true
      };

    case UserManagementActionTypes.LoadUserDetailsSuccess:
      return {
        ...state,
        usersDetails: action.payload,
      };

    case UserManagementActionTypes.LoadUsersFailure:
    case UserManagementActionTypes.LoadUserDetailsFailure:
      return {
        ...state,
        loadingError: true
      };

    default:
      return state;
  }
}

export const selectState = (state: State) => state;

export const selectUsers = createSelector(
  selectState,
  (state: State) => state
);
