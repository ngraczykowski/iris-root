import { Component, EventEmitter, HostListener, Input, OnInit, Output } from '@angular/core';
import { UserManagementClient } from '@app/templates/user-management/user-management-client';
import { User } from '@model/user.model';

@Component({
  selector: 'app-restriction-panel-users-management',
  templateUrl: './restriction-panel-users-management.component.html',
  styleUrls: ['./restriction-panel-users-management.component.scss']
})
export class RestrictionPanelUsersManagementComponent implements OnInit {

  @Input() assignedUsersList: string[];

  @Output() assignedUsersChange: EventEmitter<string[]> = new EventEmitter();

  usersList: User[];

  searchText = '';
  showSearchSuggestions: boolean;

  prefix = 'workflowManagement.workflowEditor.usersManagement.';

  constructor(
      public userManagement: UserManagementClient,
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

    for (const assignedUser of this.assignedUsersList) {
      const userData = this.usersList.find(user => user.id === Number(assignedUser));
      if (userData) {
        assignedUsersList.push(userData);
      }
    }
    return assignedUsersList;
  }

  addUserToAssignedUsersList(id) {
    this.addUserToList(id);
    this.uploadAssignedUsersList();
    this.searchText = '';
  }

  addUserToList(id) {
    if (!this.assignedUsersList.includes(id)) {
      this.assignedUsersList.push(id);
    }
  }

  removeUserFromAssignedUsersList(id) {
    this.removeUserFromList(id);
    this.uploadAssignedUsersList();
  }

  removeUserFromList(id) {
    this.assignedUsersList = this.assignedUsersList.filter(item => item !== id);
  }

  uploadAssignedUsersList() {
    this.assignedUsersChange.emit(this.assignedUsersList);
  }

  shouldShowSearchSuggestions() {
    this.showSearchSuggestions = this.searchText.length > 1;
  }
}
