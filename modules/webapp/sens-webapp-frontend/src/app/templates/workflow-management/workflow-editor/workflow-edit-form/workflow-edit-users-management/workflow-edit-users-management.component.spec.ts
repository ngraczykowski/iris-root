import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { async, ComponentFixture, getTestBed, TestBed } from '@angular/core/testing';
import { FormControl } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { CollectionResponse } from '@app/templates/model/collection-response.model';
import { UserManagementClient } from '@app/templates/user-management/user-management-client';
import { WorkflowEditFormModule } from '@app/templates/workflow-management/workflow-editor/workflow-edit-form/workflow-edit-form.module';
import { WorkflowEditFormService } from '@app/templates/workflow-management/workflow-editor/workflow-edit-form/workflow-edit-form.service';
import { TestModule } from '@app/test/test.module';
import { UserRole, UserType } from '@model/user.model';
import { Observable } from 'rxjs';

import { WorkflowEditUsersManagementComponent } from './workflow-edit-users-management.component';

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
  },
  {
    id: 2,
    userName: 'testMaker2',
    displayName: 'test2',
    roles: [UserRole.ROLE_MAKER],
    active: true,
    superUser: false,
    type: UserType.INTERNAL,
    assignments: [],
    assignmentViews: [],
  }
];

const fakeAssignedUsersList = [fakeUsersList[0].userName];

class MockWorkflowEditFormService {
  getMakersControl() {
    return new FormControl();
  }
}

class MockUserManagementClient {
  getUsers(page, size): Observable<CollectionResponse<any>> {
    return Observable.of({
      total: 1,
      results: [
        {
          id: 1,
          userName: 'test',
          displayName: 'test',
          roles: [UserRole.ROLE_MAKER],
          active: true,
          superUser: false,
          type: UserType.INTERNAL,
          assignments: [],
          assignmentViews: [],
        }
      ]
    });
  }
}


describe('WorkflowEditUsersManagementComponent', () => {
  let injector: TestBed;
  let component: WorkflowEditUsersManagementComponent;
  let fixture: ComponentFixture<WorkflowEditUsersManagementComponent>;
  let formService: WorkflowEditFormService;
  let httpTestingController: HttpTestingController;
  let httpMock: HttpTestingController;
  let userManagementClient: UserManagementClient;
  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            WorkflowEditFormModule,
            HttpClientTestingModule
          ],
          providers: [
            {
              provide: WorkflowEditFormService,
              useClass: MockWorkflowEditFormService,
            },
            UserManagementClient
          ]
        })
        .compileComponents();

    injector = getTestBed();
    httpMock = TestBed.get(HttpClientTestingModule);
    httpTestingController = injector.get(HttpTestingController);
    formService = TestBed.get(WorkflowEditFormService);
    userManagementClient = injector.get(UserManagementClient);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkflowEditUsersManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call getUsers() on ngOnInit()', () => {
    const getUsersSpy = spyOn(component, 'getUsers');
    component.ngOnInit();
    expect(getUsersSpy).toHaveBeenCalled();
  });

  it('should call UserManagementClient.getUsers() when getUsers method is called', () => {
    const getUsersSpy = spyOn(component.userManagement, 'getUsers').and.returnValue(Observable.of({
      total: 1,
      results: [
        {
          id: 1,
          userName: 'test',
          displayName: 'test',
          roles: ['ROLE_MAKER']
        }
      ]
    }));
    component.getUsers();
    expect(getUsersSpy).toHaveBeenCalledTimes(1);
    expect(component.usersList.length).toBe(1);
  });

  it('should add user to assigned users List', () => {
    const testUsername = 'testUsername';

    component.assignedUsers = [];
    fixture.detectChanges();
    component.addUserToList(testUsername);

    expect(component.assignedUsers).toEqual(jasmine.objectContaining([testUsername]));
  });

  it('should remove user form assigned users List', () => {
    const testUsername = 'testUsername';

    component.assignedUsers = fakeAssignedUsersList;
    fixture.detectChanges();
    component.removeUserFromList(testUsername);

    expect(component.assignedUsers).toEqual(jasmine.objectContaining([]));
  });

  it('should hide assigned users list when is empty', () => {
    component.assignedUsers = [];
    component.usersList = [];

    fixture.detectChanges();
    component.generateAssignedUsersList();

    expect(fixture.debugElement.query(By.css('#assigned-users-list'))).toBeNull();
  });

  it('should show assigned users list when list is not empty', () => {
    component.requiredRole = 'ROLE_MAKER';
    component.assignedUsers = ['testMaker'];
    component.usersList = fakeUsersList;
    fixture.detectChanges();
    component.generateAssignedUsersList();

    expect(fixture.debugElement.query(By.css('#assigned-users-list')).nativeElement).toBeTruthy();
  });

  it('should generate assigned users list based on "usersList"', () => {
    component.requiredRole = 'ROLE_MAKER';
    component.usersList = fakeUsersList;
    component.assignedUsers = fakeAssignedUsersList;
    fixture.detectChanges();

    component.generateAssignedUsersList();

    expect(component.generateAssignedUsersList()).toEqual([component.usersList[0]]);
  });

  it('"canBeAssigned()" should return "true" when have required role', () => {
    component.requiredRole = 'ROLE_MAKER';
    const userData = {
      roles: ['ROLE_MAKER'],
      superUser: false
    };

    expect(component.canBeAssigned(userData)).toEqual(true);
  });

  it('"canBeAssigned()" should return "true" when user is "superUser"', () => {
    const userData = {
      roles: [],
      superUser: true
    };

    expect(component.canBeAssigned(userData)).toEqual(true);
  });

  it('"canBeAssigned()" should return "false" when if the user does not have the role or "superUser" status', () => {
    const userData = {
      roles: [],
      superUser: false
    };

    expect(component.canBeAssigned(userData)).toEqual(false);
  });

  it('Should show suggestions for the users search when entering more than 1 character into the input field', () => {
    component.requiredRole = 'ROLE_MAKER';
    component.usersList = fakeUsersList;
    component.assignedUsers = fakeAssignedUsersList;
    component.searchText = '12';
    component.generateAssignedUsersList();
    component.shouldShowSearchSuggestions();

    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css('#search-suggestions')).nativeElement).toBeTruthy();
  });
});
