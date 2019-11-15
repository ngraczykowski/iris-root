import { FormBuilder, FormGroup, ValidatorFn } from '@angular/forms';
import { matchOtherValidator } from './match-other-validator';

describe('matchOtherValidator', () => {
  let form: FormGroup;
  let validator: ValidatorFn;

  beforeEach(() => {
    validator = matchOtherValidator('field1');
    form = new FormBuilder().group({
      field1: [''],
      field2: ['']
    });
  });

  it('should return null when fields are empty ', () => {
    const errors = validator(form.get('field2'));

    expect(errors).toEqual(null);
  });

  it('should return null when fields are null ', () => {
    form.get('field1').setValue(null);
    form.get('field2').setValue(null);

    const errors = validator(form.get('field2'));

    expect(errors).toEqual(null);
  });

  it('should return null when fields are equal ', () => {
    form.get('field1').setValue('value');
    form.get('field2').setValue('value');

    const errors = validator(form.get('field2'));

    expect(errors).toEqual(null);
  });


  it('should return error when fields have different values ', () => {
    form.get('field1').setValue('value1');
    form.get('field2').setValue('value2');

    const errors = validator(form.get('field2'));

    expect(errors).toEqual({mismatched: true});
  });
});
