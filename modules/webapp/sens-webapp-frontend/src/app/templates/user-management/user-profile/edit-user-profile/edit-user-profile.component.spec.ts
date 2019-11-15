import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { FormGroup } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { asyncScheduler, EMPTY, of, throwError, Observable, ReplaySubject, Subscription } from 'rxjs';
import { tap } from 'rxjs/operators';
import { EventKey } from '../../../../shared/event/event.service.model';
import { LocalEventService } from '../../../../shared/event/local-event.service';
import { TestModule } from '../../../../test/test.module';
import { User } from '../../../model/user.model';
import { UserManagementClient } from '../../user-management-client';
import { EditUserProfileComponent } from './edit-user-profile.component';
import { EditUserProfileModule } from './edit-user-profile.module';
import { EditUserProfileService } from './edit-user-profile.service';
import { EditUserTemplate } from './edit-user.model';
import { provideMockStore } from '@ngrx/store/testing';
import { StoreModule, combineReducers, Store } from '@ngrx/store';
import * as fromRoot from '@app/reducers';
import { LoadUserDetails, LoadUserDetailsSuccess } from '../../store/actions/userManagement.actions';
import { provideMockActions } from '@ngrx/effects/testing';
import { EditUserTemplateModule } from './edit-user-template/edit-user-template.module';

describe('EditUserProfileComponent', () => {
  let component: EditUserProfileComponent;
  let fixture: ComponentFixture<EditUserProfileComponent>;

  let eventService: LocalEventService;
  let editUserProfileService: EditUserProfileService;
  let translateService: TranslateService;
  let store: Store<fromRoot.State>;
  let dispatchSpy: jasmine.Spy;
  let actions$: ReplaySubject<any>;

  function mockTranslateService(translate: TranslateService) {
    spyOn(translate, 'get').and.callFake((key, value) => {
      if (key === 'user-management.data.roles.ROLE_MAKER') {
        return of('Maker');
      }

      if (key === 'user-management.userProfile.error.update.USER_ROLE_IN_USE' &&
          value.role === 'Maker' &&
          value.treeList[0] === 'DECISION_TREE_NAME1' &&
          value.treeList[1] === 'DECISION_TREE_NAME2') {
        return of('User has role Maker in trees DECISION_TREE_NAME, DECISION_TREE_NAME2.');
      }

      return of(key);
    });
  }

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            EditUserProfileModule,
            EditUserTemplateModule,
            StoreModule.forRoot({
              ...fromRoot.reducers,
              feature: combineReducers(fromRoot.reducers)
            })
          ],
          providers: [
            UserManagementClient,
            provideMockActions(() => actions$),
            provideMockStore({
              initialState: {
                userManagement: {
                  total: 1,
                  users: [
                    {
                      id: 1,
                      userName: 'user',
                      roles: ['ROLE_ANALYST']
                    }
                  ]
                }
              }
            })
          ]
        })
        .compileComponents();
    store = TestBed.get(Store);
    fixture = TestBed.createComponent(EditUserProfileComponent);
    component = fixture.componentInstance;
    eventService = TestBed.get(LocalEventService);
    editUserProfileService = TestBed.get(EditUserProfileService);
    translateService = TestBed.get(TranslateService);
    mockTranslateService(translateService);
    fixture.detectChanges();
    dispatchSpy = spyOn(store, 'dispatch');
    component.actionSubscription = new Subscription();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set `show` as true if received open event', () => {
    spyOn(editUserProfileService, 'loadUser').and.returnValue(EMPTY);
    expect(component.show).toBeFalsy();

    eventService.sendEvent({key: EventKey.OPEN_EDIT_PROFILE, data: {userId: 1}});

    expect(component.show).toBeTruthy();
  });

  it('should call LoadUserDetails action on open event', fakeAsync(() => {
    dispatchSpy.calls.reset();
    eventService.sendEvent({key: EventKey.OPEN_EDIT_PROFILE, data: {userId: 1}});
    fixture.detectChanges();
    tick();

    expect(dispatchSpy).toHaveBeenCalledTimes(1);
    expect(dispatchSpy).toHaveBeenCalledWith(new LoadUserDetails(1));
  }));

  it('should load template component on open event', fakeAsync(() => {
    const userId = 1;
    const user = <User> {userName: 'userName', roles: ['ROLE_ANALYST']};
    const form = new FormGroup({});
    dispatchSpy.calls.reset();
    actions$ = new ReplaySubject();
    actions$.next(new LoadUserDetailsSuccess(user));
    spyOn(component.templateComponent, 'load').and.returnValue(form);

    fixture.detectChanges();

    eventService.sendEvent({key: EventKey.OPEN_EDIT_PROFILE, data: {userId: userId}});
    tick();

    expect(component.templateComponent.load).toHaveBeenCalledWith(user);
  }));

  it('should reset `loadErrorMessage` after reopen component', () => {
    spyOn(editUserProfileService, 'loadUser').and.returnValue(EMPTY);
    component.loadErrorMessage = 'errorMessage';

    eventService.sendEvent({key: EventKey.OPEN_EDIT_PROFILE, data: {userId: 1}});

    expect(component.loadErrorMessage).toBeNull();
  });

  it('should reset `updateErrorMessage` after reopen component', () => {
    spyOn(editUserProfileService, 'loadUser').and.returnValue(EMPTY);
    component.updateErrorMessage = 'errorMessage';

    eventService.sendEvent({key: EventKey.OPEN_EDIT_PROFILE, data: {userId: 1}});

    expect(component.updateErrorMessage).toBeNull();
  });

  it('should set `show` as false onCancel', () => {
    component.show = true;

    component.onCancel();

    expect(component.show).toBeFalsy();
  });

  it('should disable button when template is not ready', () => {
    spyOn(component.templateComponent, 'isReady').and.returnValue(false);

    expect(component.shouldDisableSaveButton()).toBeTruthy();
  });

  it('should not disable button when template is ready', () => {
    spyOn(component.templateComponent, 'isReady').and.returnValue(true);

    expect(component.shouldDisableSaveButton()).toBeFalsy();
  });

  describe('given loaded form ready to save', () => {

    let userId: number;
    let userTemplate: EditUserTemplate;

    beforeEach(async(() => {
      userId = 1;
      userTemplate = <EditUserTemplate> {'userName': 'userName', roles: ['ROLE_ANALYST']};
      const form = new FormGroup({});
      spyOn(editUserProfileService, 'loadUser').and.returnValue(of({}));
      spyOn(component.templateComponent, 'load').and.returnValue(form);
      spyOn(component.templateComponent, 'isReady').and.returnValue(true);
      spyOn(component.templateComponent, 'getTemplate').and.returnValue(userTemplate);

      eventService.sendEvent({key: EventKey.OPEN_EDIT_PROFILE, data: {userId: userId}});
    }));

    it('should update user with valid id and template on save', () => {
      spyOn(editUserProfileService, 'updateUser').and.returnValue(EMPTY);

      component.onSave();

      expect(editUserProfileService.updateUser).toHaveBeenCalledWith(userId, userTemplate);
    });

    it('should subscribe `updateUser` task after invoke onSave', () => {
      const updateTask = of();
      spyOn(editUserProfileService, 'updateUser').and.returnValue(updateTask);
      spyOn(updateTask, 'subscribe');

      component.onSave();

      expect(updateTask.subscribe).toHaveBeenCalled();
    });

    it('should set `show` as false onSave with success', fakeAsync(() => {
      spyOn(editUserProfileService, 'updateUser').and.returnValue(of({}));
      component.show = true;

      component.onSave();
      tick();

      expect(component.show).toBeFalsy();
    }));

    it('should set `updateErrorMessage` onSave with error', fakeAsync(() => {
      spyOn(editUserProfileService, 'updateUser').and.returnValue(throwError({}));

      component.onSave();
      tick();

      expect(component.updateErrorMessage)
        .toEqual('user-management.userProfile.error.update.UNKNOWN');
    }));

    it('should reset `updateErrorMessage` onSave', () => {
      spyOn(editUserProfileService, 'updateUser').and.returnValue(EMPTY);
      component.updateErrorMessage = 'errorMessage';

      component.onSave();

      expect(component.updateErrorMessage).toBeNull();
    });

    it('should `inProgress` be true while saving', fakeAsync(() => {
      spyOn(editUserProfileService, 'updateUser')
          .and.returnValue(of({}, asyncScheduler).pipe(tap(() => tick(100))));

      component.onSave();
      expect(component.inProgress).toBeTruthy();
      tick(50);
      expect(component.inProgress).toBeTruthy();
      tick(50);
      expect(component.inProgress).toBeFalsy();
    }));

    it('should send success notification after update with success', fakeAsync(() => {
      spyOn(editUserProfileService, 'updateUser').and.returnValue(of({}));
      spyOn(eventService, 'sendEvent');

      component.onSave();
      tick();

      expect(eventService.sendEvent).toHaveBeenCalledWith({
        key: EventKey.NOTIFICATION,
        data: {
          type: 'success',
          message: 'user-management.userProfile.success.update'
        }
      });
    }));

    it('should set error message when UserIsUsedInWorkflowException', () => {
      component.updateErrorMessage = null;
      spyOn(editUserProfileService, 'updateUser').and.returnValue(throwError({
        error: {
          key: 'UserIsUsedInWorkflowException',
          extras: {
            'assignedTrees': [{
                'role': 'ROLE_MAKER',
                'treeList': ['DECISION_TREE_NAME1', 'DECISION_TREE_NAME2']
              }]
          }}}));

      component.onSave();

      expect(component.updateErrorMessage).toEqual(
          'User has role Maker in trees DECISION_TREE_NAME, DECISION_TREE_NAME2.');
    });
  });
});
