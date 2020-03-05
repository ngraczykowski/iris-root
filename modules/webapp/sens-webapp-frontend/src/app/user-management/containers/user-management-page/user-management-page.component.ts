import { Component, OnInit, OnDestroy } from '@angular/core';
import { UserManagementService } from '@app/user-management/services/user-management.service';
import { User } from '@app/user-management/models/users';
import { Subscription } from 'rxjs';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { Event, EventKey } from '@app/shared/event/event.service.model';

@Component({
  selector: 'app-user-management-page',
  templateUrl: './user-management-page.component.html',
  styleUrls: ['./user-management-page.component.scss']
})
export class UserManagementPageComponent implements OnInit, OnDestroy {
  usersList: User[];
  filteredList: User[];
  isFiltering = false;
  subscriptions: Subscription[] = [];
  filterQuery: String;
  loadingUsers = false;

  constructor(
    private readonly userManagementService: UserManagementService,
    private eventService: LocalEventService
  ) { }

  ngOnInit() {
    this.subscriptions.push(
      this.fetchUsers(),
      this.eventService.subscribe(event => {
        if ('data' in event && event.data.message === 'user-management.userProfile.success.create') {
          this.fetchUsers();
        }
      })
    );
  }

  ngOnDestroy() {
    this.subscriptions.forEach(s => s.unsubscribe());
  }

  onFilterChange(userName): void {
    if (userName.length > 0) {
      this.isFiltering = true;
      this.filteredList = this.filterUsers(userName);
      this.filterQuery = userName;
    } else {
      this.isFiltering = false;
      this.filterQuery = '';
    }
  }

  showNewUser() {
    this.eventService.sendEvent(<Event> {
      key: EventKey.OPEN_NEW_PROFILE
    });
  }

  private fetchUsers(): Subscription {
    this.loadingUsers = true;
    return this.userManagementService.getUsers()
      .subscribe(data => {
        this.usersList = data.content;
        this.loadingUsers = false;
      });
  }

  private filterUsers(userNameFilter: string): User[] {
    const isInFilterQuery = (field: String) => field.indexOf(userNameFilter) > -1;
    return this.usersList.filter((user) => isInFilterQuery(user.userName)
      || (user.displayName !== null && typeof user.displayName !== 'undefined' && isInFilterQuery(user.displayName)));
  }

}
