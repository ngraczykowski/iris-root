import { Component, OnInit, ViewChild } from '@angular/core';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { Subscription, Observable } from 'rxjs';
import { EventKey } from '@app/shared/event/event.service.model';
import { UserManagementService } from '@app/user-management/services/user-management.service';
import { UserRoles, User } from '@app/user-management/models/users';
import { UserFormComponent } from '@app/user-management/components/user-form/user-form.component';

@Component({
  selector: 'app-user-form-container',
  templateUrl: './user-form-container.component.html',
  styleUrls: ['./user-form-container.component.scss']
})
export class UserFormContainerComponent implements OnInit {
  @ViewChild('userForm', {static: true}) userFormRef: UserFormComponent;

  showModal = false;
  formValid = false;
  userRoles$: Observable<UserRoles>;
  eventServiceSubscription: Subscription;
  userData: User;

  constructor(
    private userManagementService: UserManagementService,
    private eventService: LocalEventService,
  ) { }

  ngOnInit() {
    this.userRoles$ = this.userManagementService.userRoles$;
    this.eventServiceSubscription = this.eventService.subscribe(
      () => this.showModal = true,
            [EventKey.OPEN_NEW_PROFILE]
    );
  }

  onValueChanged(data: User): void {
    this.userData = data;
  }

  onCancel() {
    this.showModal = false;
  }

  onSave() {
    this.userManagementService.createUser(this.userData).subscribe(() => {
      this.eventService.sendEvent({
        key: EventKey.NOTIFICATION,
        data: {
          type: 'success',
          message: 'user-management.userProfile.success.create'
        }
      });
      this.showModal = false;
    }, error => {
      this.userFormRef.showError(error.status);
    });
  }

  isValid(isFormValid: boolean): void {
    this.formValid = isFormValid;
  }
}
