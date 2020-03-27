import { Component, OnInit, ViewChild, OnDestroy, Input } from '@angular/core';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { UserFormComponent } from '@app/user-management/components/user-form/user-form.component';
import { User, UserRoles } from '@app/user-management/models/users';
import { UserManagementService } from '@app/user-management/services/user-management.service';
import { Observable, Subscription } from 'rxjs';

@Component({
  selector: 'app-user-form-container',
  templateUrl: './user-form-container.component.html',
  styleUrls: ['./user-form-container.component.scss']
})
export class UserFormContainerComponent implements OnInit, OnDestroy {
  @ViewChild('userForm', {static: true}) userFormRef: UserFormComponent;

  @Input() usersList = [];

  showModal = false;
  formValid = false;
  editProfile = false;
  userRoles$: Observable<UserRoles>;
  eventServiceSubscription: Subscription;
  openEditSubscription: Subscription;
  userData: User;
  resetPasswordInProgress = false;
  temporaryPassword: string;

  constructor(
    private userManagementService: UserManagementService,
    private eventService: LocalEventService,
  ) { }

  ngOnInit() {
    this.userRoles$ = this.userManagementService.userRoles$;
    this.eventServiceSubscription = this.eventService.subscribe(
      () => {
        this.showModal = true;
        this.editProfile = false;
      },
      [EventKey.OPEN_NEW_PROFILE]
    );

    this.openEditSubscription = this.eventService.subscribe(
      (eventData) => {
        this.editProfile = true;
        this.userFormRef.setEditProfileData(eventData.data);
        this.showModal = true;
      },
      [EventKey.OPEN_EDIT_PROFILE]
    );
  }

  ngOnDestroy() {
    this.eventServiceSubscription.unsubscribe();
    this.openEditSubscription.unsubscribe();
  }

  onValueChanged(data: User): void {
    this.userData = data;
  }

  onCancel() {
    this.showModal = false;
    this.temporaryPassword = null;
    this.resetPasswordInProgress = false;
    this.userFormRef.userForm.reset();
  }

  onSave() {
    if (this.editProfile) {
      this.editUser();
    } else {
      this.createUser();
    }
  }

  editUser() {
    this.userManagementService.editUser(this.userData).subscribe(() => {
      this.eventService.sendEvent({
        key: EventKey.NOTIFICATION,
        data: {
          type: 'success',
          message: 'user-management.userProfile.success.update'
        }
      });
      this.showModal = false;
      this.temporaryPassword = null;
      this.resetPasswordInProgress = false;
      this.userFormRef.userForm.reset();
    }, error => this.userFormRef.showError(error.status));
  }

  createUser() {
    this.userManagementService.createUser(this.userData).subscribe(() => {
      this.eventService.sendEvent({
        key: EventKey.NOTIFICATION,
        data: {
          type: 'success',
          message: 'user-management.userProfile.success.create'
        }
      });
      this.showModal = false;
      this.userFormRef.userForm.reset();
    }, error => {
      this.userFormRef.showError(error.status);
    });
  }

  resetPassword(userName) {
    this.resetPasswordInProgress = true;
    this.userManagementService.resetPassword(userName)
      .subscribe((res: any) => {
        this.resetPasswordInProgress = false;
        this.temporaryPassword = res.temporaryPassword;
      }, error => {
        this.resetPasswordInProgress = false;
        this.userFormRef.showError(error.status);
      });
  }

  isValid(isFormValid: boolean): void {
    this.formValid = isFormValid;
  }
}
