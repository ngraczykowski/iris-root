import { Injectable } from '@angular/core';
import { UserManagementListService } from '@core/user-managemenet/services/user-management-list.service';
import { ResetUserPasswordResponse } from '@endpoint/user-management/model/reset-user-password-response';
import { UpdateUserRequest } from '@endpoint/user-management/model/update-user-request';
import { UserManagementEndpointService } from '@endpoint/user-management/services/user-management-endpoint.service';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UserManagementService {

  constructor(private userManagementEndpointService: UserManagementEndpointService,
              private userManagementListService: UserManagementListService) {}

  public resetPassword(userName: string): Observable<ResetUserPasswordResponse> {
    return this.userManagementEndpointService.resetUserPassword(userName);
  }

  public createUser(payload: UpdateUserRequest): Observable<any> {
    payload.displayName = payload.displayName === null ? '' : payload.displayName;
    return this.userManagementEndpointService.createUser(payload).pipe(
        tap(() => this.userManagementListService.load())
    );
  }

  public updateUser(payload: UpdateUserRequest): Observable<any> {
    return this.userManagementEndpointService.updateUser(payload).pipe(
        tap(() => this.userManagementListService.load())
    );
  }

}
