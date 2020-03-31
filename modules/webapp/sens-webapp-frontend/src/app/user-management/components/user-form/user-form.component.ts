import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  Output
} from '@angular/core';
import {
  FormArray,
  FormControl,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';
import { UserValidators } from '@app/templates/user-management/user-profile/validators/user-validators';
import { User, UserRoles } from '@app/user-management/models/users';
import { UserManagementService } from '@app/user-management/services/user-management.service';
import { Subscription } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.scss']
})
export class UserFormComponent implements OnInit, OnDestroy, OnChanges {
  userNameControl = new FormControl(null, [Validators.required, Validators.compose([
    Validators.required,
    UserValidators.usernameMinLength(),
    UserValidators.usernameMaxLength(),
    UserValidators.usernameCharacters()
  ])]);
  displayNameControl = new FormControl(null, [Validators.compose([
    UserValidators.displayNameMinLength(),
    UserValidators.displayNameMaxLength()
  ])]);
  passwordControl = new FormControl(null, [Validators.required, Validators.compose([
    Validators.required,
    UserValidators.atLeastOneLetter(),
    UserValidators.atLeastOneDigit(),
    UserValidators.passwordMinLength()
  ])]);
  userForm = new FormGroup({
    userName: this.userNameControl,
    displayName: this.displayNameControl,
    password: this.passwordControl,
    roles: new FormArray([]),
  });
  valueChangesSubscription: Subscription;
  userProfilePrefix = 'usersManagement.userProfile.content.';
  adminCheckboxIndex: number;
  adminChecked = false;

  @Output() formValueChanged = new EventEmitter();
  @Output() isValid = new EventEmitter();
  @Output() resetPassword = new EventEmitter();
  @Input() usersList;
  @Input() userRoles: UserRoles;
  @Input() userData: User;
  @Input() editProfile = false;
  @Input() temporaryPassword;
  @Input() resetPasswordInProgress;

  get rolesControls() { return <FormArray>this.userForm.controls['roles']; }

  constructor(
    private userManagementService: UserManagementService,
    private translate: TranslateService
  ) { }

  ngOnInit() {
    this.onFormChanges();
  }

  ngOnDestroy() {
    this.valueChangesSubscription.unsubscribe();
  }

  ngOnChanges() {
    if (this.userRoles !== null && this.userRoles.roles.length > 0) {
      this.adminCheckboxIndex = this.userRoles.roles.findIndex(role => role.label === 'admin');
      this.getUserRolesFormControls();
    }

    if (!this.editProfile && !this.userForm.contains('password')) {
      this.userForm.addControl('password', this.passwordControl);
    }
  }

  getUsersList() {
    this.userManagementService.getUsers().subscribe(
      val => this.usersList = val.content
    );
  }

  setEditProfileData({ userData }) {
    this.userForm.controls.userName.setValue(userData.userName);
    this.userForm.controls.displayName.setValue(userData.displayName);
    const userRolesControl = <FormArray>this.userForm.get('roles');
    userData.roles.forEach((role) => {
      Object.values(this.userRoles.roles).filter((val, elementIndex) => {
        if (val.role === role) {
          userRolesControl.controls[elementIndex].setValue(true);
        }
      });
    });
    this.checkIfAdminCheckboxChecked();
    this.userForm.removeControl('password');
  }

  hasError(errorName: string, field: string): boolean {
    return this.userForm.controls[field].hasError(errorName);
  }

  showError(httpStatusCode: number): void {
    switch (httpStatusCode) {
      case 409:
        this.userForm.controls.userName.setErrors({ notUnique: true });
        break;

      case 422:
        this.userForm.setErrors({ entityProcessingError: true });
        break;

      case 507:
        this.userForm.setErrors({ insufficientStorage: true });
        break;

      case 401:
        this.userForm.setErrors({ unauthorized: true });
        break;

      case 404:
        this.userForm.setErrors({ notFound: true });
        break;

      default:
        break;
    }
  }

  isUsernameUnique(userName) {
    if (this.usersList.some(e => e.userName === userName)) {
      this.userForm.controls.userName.setErrors({ notUnique: true });
      this.isValid.emit(this.userForm.valid);
    }
  }

  checkBoxChanged() {
    this.checkIfAdminCheckboxChecked();
    this.formValueChanged.emit({ ...this.userForm.value, roles: this.userForm.controls.roles.value });
  }

  onResetPassword(userName) {
    if (confirm(
        this.translate.instant(
          'usersManagement.userProfile.content.password.popup.content',
          { user: userName }
        )
      )
    ) {
      this.resetPassword.emit(userName);
    }
  }

  private checkIfAdminCheckboxChecked() {
    if (this.userForm.controls.roles.value[this.adminCheckboxIndex]) {
      this.adminChecked = true;
      const expectedRoles = new Array(this.userRoles.roles.length).fill(true);
      this.userForm.controls.roles.setValue(expectedRoles);
    } else {
      this.adminChecked = false;
    }
  }

  private onFormChanges(): void {
    this.valueChangesSubscription = this.userForm.valueChanges.subscribe(data => {
      this.formValueChanged.emit(data);
      this.isValid.emit(this.userForm.valid);
    });
  }

  private validateRoles(): ValidatorFn {
    return (group: FormGroup): ValidationErrors => {
      if (group.value.indexOf(true) === -1) {
        const error = {};
        error['selectRole'] = true;
        return error;
      }
      return;
    };
  }

  private getUserRolesFormControls(): void {
    if (!this.editProfile && this.userForm.controls.roles.value.length === 0) {
      this.userForm.controls.roles = new FormArray([]);
      this.userRoles.roles.map(() => {
        (this.userForm.controls.roles as FormArray).push(new FormControl(false));
      });
    }
  }
}
