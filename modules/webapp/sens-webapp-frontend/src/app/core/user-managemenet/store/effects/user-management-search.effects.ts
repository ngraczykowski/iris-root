import { Injectable } from '@angular/core';
import { UserManagementSearchActionTypes } from '@core/user-managemenet/store/actions/user-management-search.actions';
import { StateWithUserManagementList } from '@core/user-managemenet/store/user-management-list.state';
import { UserManagementListItem } from '@endpoint/user-management/model/user-management-list-item';
import { Actions, Effect, ofType } from '@ngrx/effects';
import { select, Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { filter, map, switchMap, take } from 'rxjs/operators';
import { UserManagementSearchActions, UserManagementListSelectors } from '../';

@Injectable()
export class UserManagementSearchEffects {
  constructor(private actions: Actions, private store: Store<StateWithUserManagementList>) {}

  @Effect()
  search: Observable<UserManagementSearchActions.SearchResults> = this.actions.pipe(
      ofType(UserManagementSearchActionTypes.search),
      map((action: UserManagementSearchActions.Search) => action.phrase),
      switchMap((phrase: string) => {
        const phraseComparator: string = phrase.toLowerCase();
        return this.store.pipe(
            select(UserManagementListSelectors.getUserManagementList),
            filter(Boolean),
            map((list: UserManagementListItem[]) => list.filter(
                (item: UserManagementListItem) =>
                    this.userMatchesPhrase(item, phraseComparator))),
            take(1),
            map((results: UserManagementListItem[]) =>
                new UserManagementSearchActions.SearchResults(results))
        );
      })
  );

  // @TODO: use indexes to increase performance
  private userMatchesPhrase(user: UserManagementListItem, phrase: string): boolean {
    if (!phrase) {
      return true;
    }
    if (user.userName && user.userName.toLowerCase().indexOf(phrase) >= 0) {
      return true;
    }
    if (user.displayName && user.displayName.toLowerCase().indexOf(phrase) >= 0) {
      return true;
    }
    return false;
  }

}
