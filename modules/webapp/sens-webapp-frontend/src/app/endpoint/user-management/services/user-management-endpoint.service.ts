import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ResetUserPasswordResponse } from '@endpoint/user-management/model/reset-user-password-response';
import { UpdateUserRequest } from '@endpoint/user-management/model/update-user-request';
import { UserManagementListResponse } from '@endpoint/user-management/model/user-management-list-response';
import { environment } from '@env/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserManagementEndpointService {

  constructor(private http: HttpClient) {}

  list(page: number = 0, size: number = 9999): Observable<UserManagementListResponse> {
    return this.http.get<UserManagementListResponse>(`${environment.serverApiUrl}/users`, {
      params: {
          page: page.toString(),
          size: size.toString()
      }
    });
  }

  resetUserPassword(userName: string): Observable<ResetUserPasswordResponse> {
    return this.http.patch<ResetUserPasswordResponse>(`${environment.serverApiUrl}/users/${userName}/password/reset`, null);
  }

  createUser(payload: UpdateUserRequest): Observable<any> {
    return this.http.post(`${environment.serverApiUrl}/users`, payload);
  }

  updateUser(payload: UpdateUserRequest): Observable<any> {
    return this.http.patch(`${environment.serverApiUrl}/users/${payload.userName}`, payload);
  }

}
