import { Injectable } from '@angular/core';
import { StateWithUserManagementList } from '@core/user-managemenet/store/user-management-list.state';
import { UserManagementListItem } from '@endpoint/user-management/model/user-management-list-item';
import { select, Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { shareReplay, switchMap, tap } from 'rxjs/operators';
import { UserManagementListSelectors, UserManagementListActions } from '@core/user-managemenet/store';

@Injectable({
  providedIn: 'root'
})
export class UserManagementListService {

  private users: Observable<UserManagementListItem[]> = this.store.pipe(
      select(UserManagementListSelectors.getUserManagementListLoaded),
      tap((loaded: boolean) => {
        if (!loaded) {
          this.load();
        }
      }),
      switchMap(() => this.store.pipe(select(UserManagementListSelectors.getUserManagementList))),
      shareReplay()
  );

  private loading: Observable<boolean> = this.store.pipe(
      select(UserManagementListSelectors.getUserManagementListLoading),
      shareReplay()
  );

  constructor(private store: Store<StateWithUserManagementList>) { }

  public load(): void {
    this.store.dispatch(new UserManagementListActions.Load());
  }

  public getLoading(): Observable<boolean> {
    return this.loading;
  }

  public getUsers(): Observable<UserManagementListItem[]> {
    return this.users;
  }
}
