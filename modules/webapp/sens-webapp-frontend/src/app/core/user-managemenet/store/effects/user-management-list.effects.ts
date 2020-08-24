import { Injectable } from '@angular/core';
import { UserManagementListActionTypes } from '@core/user-managemenet/store/actions/user-management-list.actions';
import { UserManagementListResponse } from '@endpoint/user-management/model/user-management-list-response';
import { UserManagementEndpointService } from '@endpoint/user-management/services/user-management-endpoint.service';
import { Actions, Effect, ofType } from '@ngrx/effects';
import { catchError, map, switchMap } from 'rxjs/operators';
import {UserManagementListActions} from '../';
import { Observable, of } from 'rxjs';

@Injectable()
export class UserManagementListEffects {
  constructor(private actions: Actions,
              private userManagementEndpointService: UserManagementEndpointService) {}

  @Effect()
  load: Observable<UserManagementListActions.LoadSuccess | UserManagementListActions.LoadFail> =
      this.actions.pipe(
          ofType(UserManagementListActionTypes.load),
          switchMap(() => this.userManagementEndpointService.list().pipe(
              map((payload: UserManagementListResponse) =>
                  new UserManagementListActions.LoadSuccess(payload)),
              catchError(() => of(new UserManagementListActions.LoadFail()))
          )));
}
