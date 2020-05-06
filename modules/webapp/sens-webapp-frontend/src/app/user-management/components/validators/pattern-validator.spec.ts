import { FormControl, ValidatorFn } from '@angular/forms';
import { patternValidator } from './pattern-validator';

describe('patternValidator', () => {
  let validator: ValidatorFn;

  beforeEach(() => {
    validator = patternValidator('errorName', /[A-Z]+/);
  });

  it('should return null when field is empty ', () => {
    const errors = validator(new FormControl(''));

    expect(errors).toEqual(null);
  });

  it('should return null when field is null ', () => {
    const errors = validator(new FormControl(null));

    expect(errors).toEqual(null);
  });

  it('should return null when field matches the pattern', () => {
    const errors = validator(new FormControl('AA'));

    expect(errors).toEqual(null);
  });

  it('should return error when field does not match the pattern', () => {
    const errors = validator(new FormControl('11'));

    expect(errors).toEqual({'errorName': true});
  });
});
