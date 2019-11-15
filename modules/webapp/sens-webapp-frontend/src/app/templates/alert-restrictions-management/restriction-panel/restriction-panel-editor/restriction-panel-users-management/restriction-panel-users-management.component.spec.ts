import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, ComponentFixture, getTestBed, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { UserManagementClient } from '@app/templates/user-management/user-management-client';
import { TestModule } from '@app/test/test.module';
import { UserRole, UserType } from '@model/user.model';

import { RestrictionPanelUsersManagementComponent } from './restriction-panel-users-management.component';

const fakeUsersList = [
  {
    id: 1,
    userName: 'testMaker',
    displayName: 'test',
    roles: [UserRole.ROLE_MAKER],
    active: true,
    superUser: false,
    type: UserType.INTERNAL,
    assignments: [],
    assignmentViews: [],
  }
];

describe('RestrictionPanelUsersManagementComponent', () => {
  let component: RestrictionPanelUsersManagementComponent;
  let fixture: ComponentFixture<RestrictionPanelUsersManagementComponent>;
  let userManagementClient: UserManagementClient;
  let injector: TestBed;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        RestrictionPanelUsersManagementComponent
      ],
      imports: [
        TestModule,
        HttpClientTestingModule
      ],
      providers: [
        UserManagementClient
      ]
    })
        .compileComponents();

    injector = getTestBed();
    userManagementClient = injector.get(UserManagementClient);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RestrictionPanelUsersManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should hide assigned users list when is empty', () => {
    component.assignedUsersList = [];
    component.usersList = [];

    fixture.detectChanges();
    component.generateAssignedUsersList();

    expect(fixture.debugElement.query(By.css('#assigned-users-list'))).toBeNull();
  });

  it('should add user to assigned users List', () => {
    const userID = '5';

    component.assignedUsersList = [];
    fixture.detectChanges();
    component.addUserToList(userID);

    expect(component.assignedUsersList).toEqual(jasmine.objectContaining([userID]));
  });

  it('should remove user from assigned users List', () => {
    const userID = '5';

    component.assignedUsersList = ['5'];
    fixture.detectChanges();
    component.removeUserFromList(userID);

    expect(component.assignedUsersList).toEqual(jasmine.objectContaining([]));
  });

  it('Should show user data having only his ID', () => {
    component.usersList = fakeUsersList;
    component.assignedUsersList = ['1'];
    fixture.detectChanges();

    component.generateAssignedUsersList();

    expect(component.generateAssignedUsersList()).toEqual([component.usersList[0]]);
  });
});
