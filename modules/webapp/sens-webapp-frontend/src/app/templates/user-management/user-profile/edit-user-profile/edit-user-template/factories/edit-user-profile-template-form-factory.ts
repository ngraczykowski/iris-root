import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { User, UserRole } from '@app/templates/model/user.model';
import { UserValidators } from '../../../validators/user-validators';

export class EditUserTemplateFormFactory {

  private static getRolesForm(user: User): FormArray {
    return new FormArray(
        Object.keys(UserRole)
            .map(r => new FormControl(EditUserTemplateFormFactory.hasRole(user, r)))
    );
  }

  private static hasRole(user, role) {
    return user.roles.indexOf(<UserRole> role) !== -1;
  }

  private static getSuperUserForm(user: User): FormControl {
    return new FormControl(user.superUser);
  }

  private static createWithPassword(user: User): FormGroup {
    return new FormGroup({
      displayName: new FormControl(user.displayName),
      password: new FormControl('', Validators.compose([
        UserValidators.atLeastOneLetter(),
        UserValidators.atLeastOneDigit(),
        UserValidators.passwordMinLength()
      ])),
      repeatPassword: new FormControl('', UserValidators.matchPassword()),
      roles: this.getRolesForm(user),
      active: new FormControl(user.active),
      superUser: this.getSuperUserForm(user)
    });
  }

  private static createWithoutPassword(user: User): FormGroup {
    return new FormGroup({
      displayName: new FormControl(user.displayName),
      roles: this.getRolesForm(user),
      active: new FormControl(user.active),
      superUser: this.getSuperUserForm(user)
    });
  }

  static create(user: User, withPassword: boolean): FormGroup {
    if (withPassword) {
      return this.createWithPassword(user);
    } else {
      return this.createWithoutPassword(user);
    }
  }
}
