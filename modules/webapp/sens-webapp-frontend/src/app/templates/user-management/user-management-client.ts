import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { CollectionResponse } from '../model/collection-response.model';
import { User } from '../model/user.model';

@Injectable()
export class UserManagementClient {

  constructor(private http: HttpClient) {
  }

  updateUser(userId, template) {
    return this.http.post(`${environment.serverApiUrl}api/user/${userId}`, template);
  }

  createUser(template): Observable<any> {
    return this.http.post(`${environment.serverApiUrl}api/user`, template);
  }

  getUser(userId: number): Observable<User> {
    return this.http.get(`${environment.serverApiUrl}api/user/${userId}`)
        .pipe(map(body => <User> body));
  }

  getUsers(pageIndex?: number, size?: number): Observable<CollectionResponse<User>> {
    return this._getUsers({
      page: pageIndex,
      size: size
    });
  }

  getUserDetails(id: number): Observable<User> {
    return this.http.get(`${environment.serverApiUrl}api/user/${id}`).pipe(
      map(response => <User>response)
    );
  }

  private _getUsers(params): Observable<CollectionResponse<User>> {
    return this.http.get(environment.serverApiUrl + 'api/users',
        {params: params})
        .pipe(map(body => <CollectionResponse<User>> body));
  }
}
