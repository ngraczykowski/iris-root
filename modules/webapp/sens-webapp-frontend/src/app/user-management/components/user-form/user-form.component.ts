import { Component, OnInit, Output, OnDestroy, EventEmitter, Input, OnChanges } from '@angular/core';
import { FormGroup, FormControl, Validators, ValidatorFn, FormArray, ValidationErrors } from '@angular/forms';
import { Subscription } from 'rxjs';
import { UserRoles } from '@app/user-management/models/users';
import { UserValidators } from '@app/templates/user-management/user-profile/validators/user-validators';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.scss']
})
export class UserFormComponent implements OnInit, OnDestroy, OnChanges {
  userForm: FormGroup;
  valueChangesSubscription: Subscription;
  @Output() formValueChanged = new EventEmitter();
  @Output() isValid = new EventEmitter();
  @Input() userRoles: UserRoles;

  userProfilePrefix = 'usersManagement.userProfile.content.';

  get rolesControls() { return <FormArray>this.userForm.controls['roles']; }

  constructor() { }

  ngOnInit() {
    this.userForm = new FormGroup({
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
    this.userRoles.roles.map(() => {
      (this.userForm.controls.roles as FormArray).push(new FormControl(false));
    });
  }

}

