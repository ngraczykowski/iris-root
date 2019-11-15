import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { FormGroup } from '@angular/forms';
import { SelectEvent } from '@app/components/switch-view/switch-view.component';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { TestModule } from '@app/test/test.module';
import { asyncScheduler, EMPTY, of, throwError } from 'rxjs';
import { tap } from 'rxjs/operators';
import { UserType } from '../../../model/user.model';
import { UserManagementClient } from '../../user-management-client';
import { NewUserProfileComponent } from './new-user-profile.component';
import { NewUserProfileModule } from './new-user-profile.module';
import { NewUserProfileService } from './new-user-profile.service';
import { NewUserTemplate } from './new-user.model';

describe('NewUserProfileComponent', () => {
  let component: NewUserProfileComponent;
  let fixture: ComponentFixture<NewUserProfileComponent>;

  let eventService: LocalEventService;
  let newUserProfileService: NewUserProfileService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            NewUserProfileModule
          ],
          providers: [
            UserManagementClient
          ]
        })
        .compileComponents();

    eventService = TestBed.get(LocalEventService);
    newUserProfileService = TestBed.get(NewUserProfileService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewUserProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set `show` as true if received open event', () => {
    expect(component.show).toBeFalsy();

    eventService.sendEvent({key: EventKey.OPEN_NEW_PROFILE});

    expect(component.show).toBeTruthy();
  });

  it('should set `show` as false onCancel', () => {
    component.show = true;

    component.onCancel();

    expect(component.show).toBeFalsy();
  });

  it('should reset `errorMessage` after reopen component', () => {
    component.errorMessage = 'errorMessage';

    eventService.sendEvent({key: EventKey.OPEN_NEW_PROFILE});

    expect(component.errorMessage).toBeNull();
  });

  it('should reset `errorMessage` after switch userType', () => {
    component.errorMessage = 'errorMessage';

    component.onSelect(<SelectEvent> {item: UserType.EXTERNAL, index: 0});

    expect(component.errorMessage).toBeNull();
  });

  it('should load template on open event', () => {
    spyOn(component.templateComponent, 'load');

    eventService.sendEvent({key: EventKey.OPEN_NEW_PROFILE});

    expect(component.templateComponent.load).toHaveBeenCalledWith(UserType.INTERNAL);
  });

  it('should init template with valid userType after switch userType', () => {
    spyOn(component.templateComponent, 'load');

    component.onSelect(<SelectEvent> {item: UserType.EXTERNAL, index: 0});

    expect(component.templateComponent.load).toHaveBeenCalledWith(UserType.EXTERNAL);
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

    let userTemplate: NewUserTemplate;

    beforeEach(async(() => {
      userTemplate = <NewUserTemplate> {'userName': 'userName'};
      const form = new FormGroup({});
      spyOn(component.templateComponent, 'load').and.returnValue(form);
      spyOn(component.templateComponent, 'isReady').and.returnValue(true);
      spyOn(component.templateComponent, 'getTemplate').and.returnValue(userTemplate);

      eventService.sendEvent({key: EventKey.OPEN_NEW_PROFILE});

      component.onSelect(<SelectEvent> {item: UserType.INTERNAL, index: 1});
    }));

    it('should create user with valid id and template on save', () => {
      spyOn(newUserProfileService, 'createUser').and.returnValue(EMPTY);

      component.onSave();

      expect(newUserProfileService.createUser).toHaveBeenCalledWith(userTemplate);
    });

    it('should subscribe `createUser` task after invoke onSave', () => {
      const createTask = of();
      spyOn(newUserProfileService, 'createUser').and.returnValue(createTask);
      spyOn(createTask, 'subscribe');

      component.onSave();

      expect(createTask.subscribe).toHaveBeenCalled();
    });

    it('should set `show` as false onSave with success', fakeAsync(() => {
      spyOn(newUserProfileService, 'createUser').and.returnValue(of({}));
      component.show = true;

      component.onSave();
      tick();

      expect(component.show).toBeFalsy();
    }));


    it('should set `errorMessage` onSave with error', fakeAsync(() => {
      spyOn(newUserProfileService, 'createUser').and.returnValue(throwError({}));

      component.onSave();
      tick();

      expect(component.errorMessage).toEqual('user-management.userProfile.error.create.UNKNOWN');
    }));

    it('should reset `errorMessage` onSave', () => {
      spyOn(newUserProfileService, 'createUser').and.returnValue(EMPTY);
      component.errorMessage = 'errorMessage';

      component.onSave();

      expect(component.errorMessage).toBeNull();
    });


    it('should `inProgress` be true while saving', fakeAsync(() => {
      spyOn(newUserProfileService, 'createUser')
          .and.returnValue(of({}, asyncScheduler).pipe(tap(() => tick(100))));

      expect(component.inProgress).toBeFalsy();
      component.onSave();
      expect(component.inProgress).toBeTruthy();
      tick(50);
      expect(component.inProgress).toBeTruthy();
      tick(50);
      expect(component.inProgress).toBeFalsy();
    }));

    it('should send success notification after save with success', fakeAsync(() => {
      spyOn(newUserProfileService, 'createUser').and.returnValue(of({}));
      spyOn(eventService, 'sendEvent');

      component.onSave();
      tick();

      expect(eventService.sendEvent).toHaveBeenCalledWith({
        key: EventKey.NOTIFICATION,
        data: {
          type: 'success',
          message: 'user-management.userProfile.success.create'
        }
      });
    }));
  });
});
