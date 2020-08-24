import { UserManagementListItem } from '@endpoint/user-management/model/user-management-list-item';
import { Action } from '@ngrx/store';

export enum UserManagementSearchActionTypes {
  search = '[userManagemenetsearch] search',
  results = '[userManagemenetsearch] results',
}

export class Search implements Action {
  readonly type = UserManagementSearchActionTypes.search;
  constructor(public phrase: string) {}
}

export class SearchResults implements Action {
  readonly type = UserManagementSearchActionTypes.results;
  constructor(public payload: UserManagementListItem[]) {}
}

export type UserManagementSearchAction =
    | Search
    | SearchResults;
