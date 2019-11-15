import { AbstractControl, FormArray, FormControl, FormGroup } from '@angular/forms';
import { UserRole } from '../../../../../model/user.model';
import { NewUserTemplate } from '../../new-user.model';

class NewUserTemplateBuilder {

  private template = <NewUserTemplate> {};

  private static getSuperUser(control: FormControl) {
    return control.value;
  }

  private static getSelectedRoles(control: FormArray) {
    return Object.keys(UserRole).map(r => UserRole[r])
        .filter((r, i) => NewUserTemplateBuilder.isRoleChecked(control, i));
  }

  private static isRoleChecked(control: FormArray, index) {
    return control.at(index).value;
  }

  addUserName(control: AbstractControl) {
    this.template.userName = control.value;
    return this;
  }

  addPassword(control: AbstractControl) {
    this.template.password = control.value;
    return this;
  }

  addDisplayName(control: AbstractControl) {
    this.template.displayName = control.value;
    return this;
  }

  addRoles(control: AbstractControl) {
    this.template.roles = NewUserTemplateBuilder.getSelectedRoles(<FormArray> control);
    return this;
  }

  addSuperUser(control: AbstractControl) {
    this.template.superUser = NewUserTemplateBuilder.getSuperUser(<FormControl> control);
    return this;
  }

  build() {
    return this.template;
  }
}

export class NewUserTemplateFactory {

  private static createWithPassword(form: FormGroup): NewUserTemplate {
    return new NewUserTemplateBuilder()
        .addUserName(form.get('userName'))
        .addDisplayName(form.get('displayName'))
        .addPassword(form.get('password'))
        .addRoles(form.get('roles'))
        .addSuperUser(form.get('superUser'))
        .build();
  }

  private static createWithoutPassword(form: FormGroup): NewUserTemplate {
    return new NewUserTemplateBuilder()
        .addUserName(form.get('userName'))
        .addDisplayName(form.get('displayName'))
        .addRoles(form.get('roles'))
        .addSuperUser(form.get('superUser'))
        .build();
  }

  static create(form: FormGroup, withPassword: boolean): NewUserTemplate {
    if (withPassword) {
      return this.createWithPassword(form);
    } else {
      return this.createWithoutPassword(form);
    }
  }
}
