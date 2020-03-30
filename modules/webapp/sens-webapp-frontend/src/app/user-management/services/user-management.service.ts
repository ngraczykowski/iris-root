import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@env/environment';
import { BehaviorSubject, Observable } from 'rxjs';
import { User, UserResponse, UserRoles, UserRolesResponse } from '../models/users';

@Injectable()
export class UserManagementService {
  userRoles$ = new BehaviorSubject<UserRoles>(null);
  adminIndex;

  constructor(
    private http: HttpClient
  ) {
    this.getUserRoles().subscribe(response => {
      const mappedRoles = response.roles.map((role, index) => {
        if (role === 'Admin') {
          this.adminIndex = index;
        }

        let translationLabel = role.replace(/\s/g, '');
        translationLabel = translationLabel.charAt(0).toLowerCase() + translationLabel.substring(1);
        return {role: role, label: translationLabel};
      });
      this.userRoles$.next({roles: mappedRoles});
    });
  }

  getUsers(): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${environment.serverApiUrl}/users?page=0&size=9999`);
  }

  getUserRoles(): Observable<UserRolesResponse> {
    return this.http.get<UserRolesResponse>(`${environment.serverApiUrl}/users/roles`);
  }

  createUser(payload: User) {
    payload.roles = this.mapToRoles(payload.roles);
    payload.displayName = payload.displayName === null ? '' : payload.displayName;
    return this.http.post(`${environment.serverApiUrl}/users`, payload);
  }

  editUser(payload: User) {
    payload.roles = this.mapToRoles(payload.roles);
    return this.http.patch(`${environment.serverApiUrl}/users/${payload.userName}`, payload);
  }

  resetPassword(userName: string) {
    return this.http.patch(`${environment.serverApiUrl}/users/${userName}/password/reset`, null);
  }

  private mapToRoles(rolesPayload: Array<String | boolean>): String[] {
    const mappedRoles = [];

    if (rolesPayload[this.adminIndex]) {
      mappedRoles.push('Admin');
    } else {
      rolesPayload.forEach((role, index) => role ? mappedRoles.push(this.userRoles$.value.roles[index].role) : false);
    }

    return mappedRoles;
  }
}
