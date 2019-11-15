import { Injectable } from '@angular/core';
import { Actions, Effect, ofType } from '@ngrx/effects';
import { map, catchError, exhaustMap } from 'rxjs/operators';
import { of, Observable } from 'rxjs';
import { Action } from '@ngrx/store';
import { UserManagementActionTypes,
        LoadUserDetails,
        LoadUsersSuccess,
        LoadUserDetailsSuccess,
        LoadUsersFailure,
        LoadUserDetailsFailure } from '../actions/userManagement.actions';
import { UserManagementClient } from '../../user-management-client';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { EventKey } from '@app/shared/event/event.service.model';

@Injectable()
export class UserManagementEffects {

  @Effect()
  loadUsers$: Observable<Action> = this.actions$.pipe(
    ofType(UserManagementActionTypes.LoadUsers),
    exhaustMap(() => this.userManagementClient.getUsers(0, 9999)),
    map(data => new LoadUsersSuccess(data)),
    catchError(() => of(new LoadUsersFailure()))
  );

  @Effect()
  loadUserDetails$: Observable<Action> = this.actions$.pipe(
    ofType(UserManagementActionTypes.LoadUserDetails),
    exhaustMap((action: LoadUserDetails) => this.userManagementClient.getUserDetails(action.payload)),
    map(data => new LoadUserDetailsSuccess(data)),
    catchError(() => of(new LoadUserDetailsFailure()))
  );

  @Effect({dispatch: false})
  loadDataFailure$ = this.actions$.pipe(
    ofType(UserManagementActionTypes.LoadUserDetailsFailure || UserManagementActionTypes.LoadUsersFailure),
    map(() => this.eventService.sendEvent({
      key: EventKey.SHOW_ERROR_WINDOW
    }))
  );

  constructor(
    private actions$: Actions,
    private eventService: LocalEventService,
    private userManagementClient: UserManagementClient
  ) {}
}
