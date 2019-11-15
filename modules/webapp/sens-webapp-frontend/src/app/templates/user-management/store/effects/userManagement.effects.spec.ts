import { TestBed } from '@angular/core/testing';
import { provideMockActions } from '@ngrx/effects/testing';
import { Observable } from 'rxjs';
import { UserManagementEffects } from './userManagement.effects';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { cold, hot } from 'jasmine-marbles';
import { LoadUsers, LoadUserDetailsSuccess, LoadUsersSuccess, LoadUserDetails } from '../actions/userManagement.actions';
import { UserManagementClient } from '../../user-management-client';
import { LocalEventService } from '@app/shared/event/local-event.service';

describe('UserManagementEffects', () => {
  let actions$: Observable<any>;
  let effects: UserManagementEffects;
  let userManagementClient: jasmine.SpyObj<UserManagementClient>;
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        UserManagementEffects,
        provideMockActions(() => actions$),
        LocalEventService,
        {
          provide: UserManagementClient,
          useValue: {
            getUsers: jasmine.createSpy(),
            getUserDetails: jasmine.createSpy()
          }
        }
      ]
    });

    effects = TestBed.get(UserManagementEffects);
    userManagementClient = TestBed.get(UserManagementClient);
  });

  describe('loadUsers$', () => {
    it('should return a stream with users loaded action', () => {
      const responseData = [{
        id: 1,
        name: 'user test'
      }];
      const action = new LoadUsers();
      const result = new LoadUsersSuccess(responseData);

      actions$ = hot('-a', {a: action});
      const response = cold('-a|', {a: responseData});
      const expected = cold('--b', {b: result});
      userManagementClient.getUsers.and.returnValue(response);

      expect(effects.loadUsers$).toBeObservable(expected);
    });
  });

  describe('loadUsersDetails$', () => {
    it('should return a stream with user details loaded action', () => {
      const responseData = [{
        id: 1,
        name: 'user test'
      }];
      const action = new LoadUserDetails(1);
      const result = new LoadUserDetailsSuccess(responseData);

      actions$ = hot('-a', {a: action});
      const response = cold('-a|', {a: responseData});
      const expected = cold('--b', {b: result});
      userManagementClient.getUserDetails.and.returnValue(response);

      expect(effects.loadUserDetails$).toBeObservable(expected);
    });
  });
});
