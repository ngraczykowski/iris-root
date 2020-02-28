import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { environment } from '@env/environment';
import { User, UserResponse, UserRoles, UserRolesResponse } from '../models/users';

@Injectable({
  providedIn: 'root'
})
export class UserManagementService {
  userRoles$ = new BehaviorSubject<UserRoles>(null);

  constructor(
    private http: HttpClient
  ) {
    this.getUserRoles().subscribe(response => {
      const mappedRoles = response.roles.map(role => {
        let translationLabel = role.replace(/\s/g, '');
        translationLabel = translationLabel.charAt(0).toLowerCase() + translationLabel.substring(1);
        return { role: role, label: translationLabel };
      });
      this.userRoles$.next({ roles: mappedRoles });
    });
  }

  getUsers(): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${environment.serverApiUrl}/users`);
  }

  getUserRoles(): Observable<UserRolesResponse> {
    return this.http.get<UserRolesResponse>(`${environment.serverApiUrl}/users/roles`);
  }

  createUser(payload: User) {
    payload.roles = this.mapToRoles(payload.roles);
    return this.http.post(`${environment.serverApiUrl}/users`, payload);
  }

  editUser(payload: User) {
    return this.http.post(`${environment.serverApiUrl}/users`, payload);
  }

  private mapToRoles(rolesPayload: String[]): String[] {
    const mappedRoles = [];
    rolesPayload.forEach((role, index) => role ? mappedRoles.push(this.userRoles$.value.roles[index].role) : false);
    return mappedRoles;
  }
}
