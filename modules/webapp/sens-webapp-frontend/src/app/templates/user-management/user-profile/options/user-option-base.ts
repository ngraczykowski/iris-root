import { AbstractControl } from '@angular/forms';
import EnumHelper from '../../../../shared/helpers/enum-helper';

export abstract class UserOptionBase {

  readonly optionId = Math.random().toString(36).substr(2, 9);

  shouldDisplayErrors(control: AbstractControl): boolean {
    return control.dirty && control.errors != null;
  }

  getErrors(prefix, control: AbstractControl): string[] {
    if (control.errors != null) {
      return Object.keys(control.errors).map(k => `${prefix}.${k}`);
    } else {
      return [];
    }
  }
}
