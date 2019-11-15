import { FormArray, FormControl, FormGroup, Validators, ValidatorFn } from '@angular/forms';
import { UserRole } from '../../../../../model/user.model';
import { UserValidators } from '../../../validators/user-validators';

export class NewUserTemplateFormFactory {

  private static getRolesForm(): FormArray {
    return new FormArray(Object.keys(UserRole).map(() => new FormControl(false)));
  }

  private static getSuperUserForm(): FormControl {
    return new FormControl(false);
  }

  private static createWithPassword(): FormGroup {
    return new FormGroup({
      displayName: new FormControl(''),
      userName: new FormControl('', this.getUserNameValidators()),
      password: new FormControl('', Validators.compose([
        Validators.required,
        UserValidators.atLeastOneLetter(),
        UserValidators.atLeastOneDigit(),
        UserValidators.passwordMinLength()
      ])),
      repeatPassword: new FormControl('', Validators.compose([
        Validators.required,
        UserValidators.matchPassword()
      ])),
      roles: this.getRolesForm(),
      superUser: this.getSuperUserForm()
    });
  }

  private static createWithoutPassword(): FormGroup {
    return new FormGroup({
      displayName: new FormControl(''),
      userName: new FormControl('', this.getUserNameValidators()),
      roles: this.getRolesForm(),
      superUser: this.getSuperUserForm()
    });
  }

  private static getUserNameValidators(): ValidatorFn {
    return Validators.compose([
      Validators.required,
      UserValidators.usernameMinLength(),
      Validators.pattern(/^\S*$/)
    ]);
  }

  static create(withPassword: boolean): FormGroup {
    if (withPassword) {
      return this.createWithPassword();
    } else {
      return this.createWithoutPassword();
    }
  }
}
