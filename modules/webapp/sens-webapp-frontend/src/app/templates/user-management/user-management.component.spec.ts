import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Event, EventKey } from '../../shared/event/event.service.model';
import { LocalEventService } from '../../shared/event/local-event.service';
import { WINDOW } from '../../shared/window.service';
import { TestModule } from '../../test/test.module';
import { UserManagementComponent } from './user-management.component';
import { StoreModule } from '@ngrx/store';
import { Actions } from '@ngrx/effects';
import { UserManagementClient } from './user-management-client';
import { NewUserProfileModule } from './user-profile/new-user-profile/new-user-profile.module';
import { EditUserProfileModule } from './user-profile/edit-user-profile/edit-user-profile.module';
import { UserTableModule } from './user-table/user-table.module';
import { provideMockStore } from '@ngrx/store/testing';

const MockWindow = {
  location: {
    assign(param) { }
  }
};

describe('UserManagementComponent', () => {
  let component: UserManagementComponent;
  let fixture: ComponentFixture<UserManagementComponent>;

  let eventService: LocalEventService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            NewUserProfileModule,
            EditUserProfileModule,
            UserTableModule,
            StoreModule.forRoot({}),
          ],
          providers: [
            UserManagementClient,
            LocalEventService,
            Actions,
            {
              provide: WINDOW, useValue: MockWindow
            },
            provideMockStore({
              initialState: {
                userManagement: {
                  total: 1,
                  users: [
                    {id: 1, userName: 'user'}
                  ]
                }
              }
            })
          ],
          declarations: [
            UserManagementComponent
          ]
        })
        .compileComponents();

    eventService = TestBed.get(LocalEventService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should send open new profile event when invoke onAddNewUser', () => {
    spyOn(eventService, 'sendEvent');

    component.onAddNewUser();

    expect(eventService.sendEvent).toHaveBeenCalledWith(<Event> {key: EventKey.OPEN_NEW_PROFILE});
  });

  it('should call open with user audit-trial report url, when onDownloadUserList invoked', () => {
    const spy = spyOn(component.window.location, 'assign');

    component.onDownloadUserList();
    expect(spy).toHaveBeenCalledWith('/api/users/export');
  });
});
