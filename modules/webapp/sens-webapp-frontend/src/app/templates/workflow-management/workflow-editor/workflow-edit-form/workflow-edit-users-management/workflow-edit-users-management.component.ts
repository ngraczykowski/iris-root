import { HttpClient } from '@angular/common/http';
import { Component, EventEmitter, HostListener, Input, OnInit, Output } from '@angular/core';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { UserManagementClient } from '@app/templates/user-management/user-management-client';
import { User } from '@model/user.model';
import { WorkflowEditFormService } from '../workflow-edit-form.service';

@Component({
  selector: 'app-workflow-edit-users-management',
  templateUrl: './workflow-edit-users-management.component.html',
  styleUrls: ['./workflow-edit-users-management.component.scss'],
  providers: [UserManagementClient]
})
export class WorkflowEditUsersManagementComponent implements OnInit {

  @Input() assignedUsers: string[];
  @Input() requiredRole: string;

  @Output() assignedUsersChange: EventEmitter<string[]> = new EventEmitter();

  usersList: User[];

  searchText = '';
  showSearchSuggestions: boolean;

  prefix = 'workflowManagement.workflowEditor.usersManagement.';

  constructor(
      private http: HttpClient,
      private formService: WorkflowEditFormService,
      public userManagement: UserManagementClient,
      private eventService: LocalEventService,
  ) { }

  @HostListener('document:click', ['$event']) clickout(event) {
    this.showSearchSuggestions = false;
  }

  handleAsideClick(event: Event) {
    event.stopPropagation();
  }

  ngOnInit() {
    this.getUsers();
  }

  getUsers() {
    this.userManagement.getUsers(0, 9999).subscribe((response) => {
      this.usersList = response.results;
    });
  }

  generateAssignedUsersList() {
    const assignedUsersList: any[] = [];

    for (const assignedUser of this.assignedUsers) {
      const userData = this.usersList.find(user => user.userName === assignedUser);
      if (userData) {
        assignedUsersList.push(userData);
      }
    }
    return assignedUsersList;
  }

  addUserToAssignedUsersList(userName) {
    this.addUserToList(userName);
    this.uploadAssignedUsersList();
    this.searchText = '';
  }

  addUserToList(userName) {
    if (!this.assignedUsers.includes(userName)) {
      this.assignedUsers.push(userName);

    }
  }

  removeUserFromAssignedUsersList(userName) {
    this.removeUserFromList(userName);
    this.uploadAssignedUsersList();
  }

  removeUserFromList(userName) {
    const temporaryAssignedUsersList = [...this.assignedUsers];
    this.assignedUsers = temporaryAssignedUsersList.filter(item => item !== userName);
  }

  uploadAssignedUsersList() {
    this.assignedUsersChange.emit(this.assignedUsers);
    this.formService.form.markAsDirty();
  }

  addMissingRole(userData) {
    const userID: number = userData.id;
    const userRoles: string[] = userData.roles;

    userRoles.push(this.requiredRole);
    const request = {roles: userRoles};
    this.userManagement.updateUser(userID, request).subscribe();
    this.sendSuccessEvent( this.prefix + 'briefMessage.addedMakerRole');
  }

  canBeAssigned(userData) {
    const isSuperUser: boolean = userData.superUser;
    const isMaker: boolean = userData.roles.includes(this.requiredRole);

    return isSuperUser || isMaker;
  }

  getMissingRoleName() {
    return this.requiredRole.toLowerCase().replace(/_/gi, ' ');
  }

  shouldShowSearchSuggestions() {
    this.showSearchSuggestions = this.searchText.length > 1;
  }

  private sendSuccessEvent(message) {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        type: 'success',
        message: message
      }
    });
  }
}
