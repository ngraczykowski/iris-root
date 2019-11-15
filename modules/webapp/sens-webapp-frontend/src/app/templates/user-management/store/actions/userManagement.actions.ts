import { Action } from '@ngrx/store';

export enum UserManagementActionTypes {
  LoadUsers = '[User Management] Load users',
  LoadUsersSuccess = '[User Management] Load users success',
  LoadUsersFailure = '[User Management] Load users failure',
  LoadUserDetails = '[User Management] Load user details',
  LoadUserDetailsSuccess = '[User Management] Load user details success',
  LoadUserDetailsFailure = '[User Management] Load user details failure',
}

export class LoadUsers implements Action {
  readonly type = UserManagementActionTypes.LoadUsers;
}

export class LoadUsersSuccess implements Action {
  readonly type = UserManagementActionTypes.LoadUsersSuccess;

  constructor(public payload: any) {}
}

export class LoadUsersFailure implements Action {
  readonly type = UserManagementActionTypes.LoadUsersFailure;
}

export class LoadUserDetails implements Action {
  readonly type = UserManagementActionTypes.LoadUserDetails;

  constructor(public payload: number) {}
}

export class LoadUserDetailsSuccess implements Action {
  readonly type = UserManagementActionTypes.LoadUserDetailsSuccess;

  constructor(public payload: any) {}
}

export class LoadUserDetailsFailure implements Action {
  readonly type = UserManagementActionTypes.LoadUserDetailsFailure;
}

export type UserManagementActions = LoadUsers
  | LoadUsersSuccess
  | LoadUsersFailure
  | LoadUserDetails
  | LoadUserDetailsSuccess
  | LoadUserDetailsFailure;
