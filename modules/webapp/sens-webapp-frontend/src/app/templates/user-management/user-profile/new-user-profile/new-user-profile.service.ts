import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import 'rxjs-compat/add/operator/delay';
import { UserManagementClient } from '../../user-management-client';
import { NewUserTemplate } from './new-user.model';

@Injectable()
export class NewUserProfileService {

  constructor(private userManagementClient: UserManagementClient) {}

  createUser(userTemplate: NewUserTemplate): Observable<any> {
    return this.userManagementClient.createUser(userTemplate);
  }
}
