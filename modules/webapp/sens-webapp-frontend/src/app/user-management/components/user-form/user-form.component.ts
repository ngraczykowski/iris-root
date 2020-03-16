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
import { UserRoles } from '@app/user-management/models/users';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.scss']
})
export class UserFormComponent implements OnInit, OnDestroy, OnChanges {
  userForm = new FormGroup({
    userName: new FormControl(null, [Validators.required, Validators.compose([
      Validators.required,
      UserValidators.usernameMinLength(),
      UserValidators.usernameMaxLength(),
      UserValidators.usernameCharacters()
    ])]),
    displayName: new FormControl(null, [Validators.compose([
      UserValidators.displayNameMinLength(),
      UserValidators.displayNameMaxLength()
    ])]),
    password: new FormControl(null, [Validators.required, Validators.compose([
      Validators.required,
      UserValidators.atLeastOneLetter(),
      UserValidators.atLeastOneDigit(),
      UserValidators.passwordMinLength()
    ])]),
    roles: new FormArray([]),
  });
  valueChangesSubscription: Subscription;
  userProfilePrefix = 'usersManagement.userProfile.content.';

  @Output() formValueChanged = new EventEmitter();
  @Output() isValid = new EventEmitter();
  @Input() usersList;
  @Input() userRoles: UserRoles;

  get rolesControls() { return <FormArray>this.userForm.controls['roles']; }

  constructor() { }

  ngOnInit() {
    this.onFormChanges();
  }

  ngOnDestroy() {
    this.valueChangesSubscription.unsubscribe();
  }

  ngOnChanges() {
    if (this.userRoles !== null && this.userRoles.roles.length > 0) {
      this.getUserRolesFormControls();
    }
  }

  hasError(errorName: string, field: string): boolean {
    return this.userForm.controls[field].hasError(errorName);
  }

  showError(httpStatusCode: number): void {
    if (httpStatusCode === 409) {
      this.userForm.controls.userName.setErrors({notUnique: true});
    } else {
      this.userForm.setErrors({entityProcessingError: true});
    }
  }

  isUsernameUnique(userName) {
    if (this.usersList.some(e => e.userName === userName)) {
      this.userForm.controls.userName.setErrors({notUnique: true});
      this.isValid.emit(this.userForm.valid);
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
    this.userForm.controls.roles = new FormArray([]);
    this.userRoles.roles.map(() => {
      (this.userForm.controls.roles as FormArray).push(new FormControl(false));
    });
  }

}
