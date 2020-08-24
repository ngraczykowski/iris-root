import { UserManagementListState } from '@core/user-managemenet/store/user-management-list.state';
import {
  StateWithUserManagementSearch,
  USER_MANAGEMENT_SEARCH_STATE_NAME
} from '@core/user-managemenet/store/user-management-search.state';
import { UserManagementListItem } from '@endpoint/user-management/model/user-management-list-item';
import { createFeatureSelector, createSelector, MemoizedSelector } from '@ngrx/store';

export const getUserManagementSearchState: MemoizedSelector<StateWithUserManagementSearch,
    UserManagementListState> = createFeatureSelector(USER_MANAGEMENT_SEARCH_STATE_NAME);

export const getUserManagementSearchResults: MemoizedSelector<StateWithUserManagementSearch,
    UserManagementListItem[]> = createSelector(getUserManagementSearchState,
    (state: UserManagementListState) => state.list);
