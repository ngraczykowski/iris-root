import { Injectable } from '@angular/core';
import { UserManagementListService } from '@core/user-managemenet/services/user-management-list.service';
import { StateWithUserManagementSearch } from '@core/user-managemenet/store/user-management-search.state';
import { UserManagementListItem } from '@endpoint/user-management/model/user-management-list-item';
import { select, Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { finalize, shareReplay, switchMap, tap } from 'rxjs/operators';
import { UserManagementSearchActions, UserManagementSearchSelectors } from '../store';

@Injectable({
  providedIn: 'root'
})
export class UserManagementSearchService {

  private phrase: string = '';
  private results: Observable<UserManagementListItem[]> = this.userManagementListService.getUsers()
      .pipe(
          tap(() => this.store.dispatch(new UserManagementSearchActions.Search(this.phrase))),
          finalize(() => this.search('')),
          switchMap(() => this.store.pipe(
              select(UserManagementSearchSelectors.getUserManagementSearchResults))),
          shareReplay({ bufferSize: 1, refCount: true })
      );

  constructor(private store: Store<StateWithUserManagementSearch>,
              private userManagementListService: UserManagementListService) {}

  public getLoading(): Observable<boolean> {
    return this.userManagementListService.getLoading();
  }

  public getResults(): Observable<UserManagementListItem[]> {
    return this.results;
  }

  public search(phrase: string): void {
    this.phrase = phrase;
    this.store.dispatch(new UserManagementSearchActions.Search(phrase));
  }
}
