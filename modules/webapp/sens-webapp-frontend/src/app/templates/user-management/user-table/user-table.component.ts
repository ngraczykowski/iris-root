import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { PageableDynamicTableComponent } from '@app/components/pageable-dynamic-table/pageable-dynamic-table.component';
import * as fromRoot from '@app/reducers/index';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { Store } from '@ngrx/store';
import { BehaviorSubject, Subscription } from 'rxjs';
import { LoadUsers } from '../store/actions/userManagement.actions';
import { UserTableDataProvider } from './user-table-data-provider';

@Component({
  selector: 'app-user-table',
  templateUrl: './user-table.component.html',
  styleUrls: ['./user-table.component.scss']
})
export class UserTableComponent implements OnInit, OnDestroy {
  @ViewChild(PageableDynamicTableComponent, {static: false}) table: PageableDynamicTableComponent;
  usersLoaded$ = this.store.select(fromRoot.getUsersLoaded);
  userNameFilter: String;

  private dataLoading$ = new BehaviorSubject(false);

  private readonly notifications = [
    'user-management.userProfile.success.create',
    'user-management.userProfile.success.update'
  ];

  eventSubscription: Subscription;

  constructor(
    private store: Store<fromRoot.State>,
    private eventService: LocalEventService,
    public userTableDataProvider: UserTableDataProvider
  ) { }

  ngOnInit() {
    this.store.dispatch(new LoadUsers);
    this.eventSubscription = this.eventService.subscribe(event => {
      if (this.notifications.indexOf(event.data.message) > -1) {
        this.table.provider = this.userTableDataProvider;
      }
    }, [EventKey.NOTIFICATION]);
  }

  ngOnDestroy() {
    this.eventSubscription.unsubscribe();
  }

  onFilterChange(userName): void {
    if (userName.length > 0) {
      this.table.filterData(userName);
    } else {
      this.table.resetData();
    }
  }

  loading($event: boolean) {
    this.dataLoading$.next($event);
  }

}
