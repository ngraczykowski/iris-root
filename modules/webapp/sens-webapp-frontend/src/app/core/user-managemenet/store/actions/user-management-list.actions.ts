import { UserManagementListResponse } from '@endpoint/user-management/model/user-management-list-response';
import { Action } from '@ngrx/store';

export enum UserManagementListActionTypes {
  load = '[userManagemenetList] load',
  loadSuccess = '[userManagemenetList] load success',
  loadFail = '[userManagemenetList] load fail'
}

export class Load implements Action {
  readonly type = UserManagementListActionTypes.load;
}

export class LoadSuccess implements Action {
  readonly type = UserManagementListActionTypes.loadSuccess;
  constructor(public payload: UserManagementListResponse) {}
}

export class LoadFail implements Action {
  readonly type = UserManagementListActionTypes.loadFail;
}

export type UserManagementListAction =
    | Load
    | LoadSuccess
    | LoadFail;
