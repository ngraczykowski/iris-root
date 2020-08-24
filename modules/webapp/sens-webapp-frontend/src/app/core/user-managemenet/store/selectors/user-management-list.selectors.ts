import {
  StateWithUserManagementList, USER_MANAGEMENT_LIST_STATE_NAME,
  UserManagementListState
} from '@core/user-managemenet/store/user-management-list.state';
import { UserManagementListItem } from '@endpoint/user-management/model/user-management-list-item';
import { createFeatureSelector, createSelector, MemoizedSelector } from '@ngrx/store';

export const getUserManagementListState: MemoizedSelector<StateWithUserManagementList,
    UserManagementListState> = createFeatureSelector(USER_MANAGEMENT_LIST_STATE_NAME);


export const getUserManagementListLoading: MemoizedSelector<StateWithUserManagementList,
    boolean> = createSelector(getUserManagementListState,
      (state: UserManagementListState) => state.loading);

export const getUserManagementListLoaded: MemoizedSelector<StateWithUserManagementList,
    boolean> = createSelector(getUserManagementListState,
    (state: UserManagementListState) => state.loaded);

export const getUserManagementList: MemoizedSelector<StateWithUserManagementList,
    UserManagementListItem[]> = createSelector(getUserManagementListState,
    (state: UserManagementListState) => state.list);
