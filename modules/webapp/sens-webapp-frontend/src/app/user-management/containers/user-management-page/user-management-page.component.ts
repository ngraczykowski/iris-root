import { Component, OnInit } from '@angular/core';
import { UserManagementService } from '@app/user-management/services/user-management.service';
import { Users } from '@app/user-management/models/users';

@Component({
  selector: 'app-user-management-page',
  templateUrl: './user-management-page.component.html',
  styleUrls: ['./user-management-page.component.scss']
})
export class UserManagementPageComponent implements OnInit {
  usersList: Users[];
  filteredList: Users[];
  isFiltering = false;

  constructor(
    private readonly userManagementService: UserManagementService
  ) { }

  ngOnInit() {
    this.userManagementService.getUsers().subscribe(data => this.usersList = data.content);
  }

  onFilterChange(userName): void {
    if (userName.length > 0) {
      this.isFiltering = true;
      this.filteredList = this.filterUsers(userName);
    } else {
      this.isFiltering = false;
    }
  }

  private filterUsers(userNameFilter: string): Users[] {
    const isInFilterQuery = (field: string) => field.indexOf(userNameFilter) > -1;
    return this.usersList.filter((user) => isInFilterQuery(user.userName)
      || (user.displayName !== null && isInFilterQuery(user.displayName)));
  }

}
