import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import 'rxjs-compat/add/operator/delay';
import { User } from '../../../model/user.model';
import { UserManagementClient } from '../../user-management-client';
import { EditUserTemplate } from './edit-user.model';

@Injectable()
export class EditUserProfileService {

  constructor(private userManagementClient: UserManagementClient) {}

  loadUser(userId: number): Observable<User> {
    return this.userManagementClient.getUser(userId);
  }

  updateUser(userId: number, userTemplate: EditUserTemplate): Observable<any> {
    return this.userManagementClient.updateUser(userId, userTemplate);
  }
}
