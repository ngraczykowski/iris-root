import { AbstractControl, FormArray, FormControl, FormGroup } from '@angular/forms';
import { UserRole } from '../../../../../model/user.model';
import { EditUserTemplate } from '../../edit-user.model';

class EditUserTemplateBuilder {

  private template: EditUserTemplate = {};

  private static getSuperUser(control: FormControl) {
    return control.value;
  }

  private static isRoleChecked(control: FormArray, index) {
    return control.at(index).value;
  }

  addPassword(control: AbstractControl) {
    if (control.dirty && control.touched) {
      this.template.password = control.value;
    }
    return this;
  }

  addDisplayName(control: AbstractControl) {
    if (control.dirty && control.touched) {
      this.template.displayName = control.value;
    }
    return this;
  }

  addRoles(control: AbstractControl) {
    if (control.dirty && control.touched) {
      this.template.roles = Object.keys(UserRole)
          .filter((r, i) => EditUserTemplateBuilder.isRoleChecked(<FormArray> control, i))
          .map(r => <UserRole> r);
    }
    return this;
  }

  addActive(control: AbstractControl) {
    if (control.dirty && control.touched) {
      this.template.active = control.value;
    }
    return this;
  }

  addSuperUser(control: AbstractControl) {
    if (control.dirty && control.touched) {
      this.template.superUser = EditUserTemplateBuilder.getSuperUser(<FormControl> control);
    }
    return this;
  }

  build(): EditUserTemplate {
    return this.template;
  }
}

export class EditUserTemplateFactory {

  private static createWithPassword(form: FormGroup): EditUserTemplate {
    return new EditUserTemplateBuilder()
        .addPassword(form.get('password'))
        .addDisplayName(form.get('displayName'))
        .addRoles(form.get('roles'))
        .addActive(form.get('active'))
        .addSuperUser(form.get('superUser'))
        .build();
  }

  private static createWithoutPassword(form: FormGroup): EditUserTemplate {
    return new EditUserTemplateBuilder()
        .addRoles(form.get('roles'))
        .addDisplayName(form.get('displayName'))
        .addActive(form.get('active'))
        .addSuperUser(form.get('superUser'))
        .build();
  }

  static create(form: FormGroup, withPassword: boolean): EditUserTemplate {
    if (withPassword) {
      return this.createWithPassword(form);
    } else {
      return this.createWithoutPassword(form);
    }
  }
}
