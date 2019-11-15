import { FormBuilder, FormControl } from '@angular/forms';
import { UserOptionBase } from './user-option-base';

class TestUserOption extends UserOptionBase {

}

describe('UserOptionBase', () => {
  let form: FormControl;
  let optionBase;

  beforeEach(() => {
    optionBase = new TestUserOption();
    form = new FormBuilder().control('');
  });

  it('should return empty array if errors are null', () => {
    form.setErrors(null);

    expect(optionBase.getErrors('prefix', form))
        .toEqual([]);
  });

  it('should return valid errors with prefix if control has errors', () => {
    form.setErrors({mismatched: true, minlength: true});

    expect(optionBase.getErrors('prefix', form))
        .toEqual(['prefix.mismatched', 'prefix.minlength']);
  });

  it('should `shouldDisplayErrors()` return true when there are errors and form is dirty', () => {
    form.setErrors({mismatched: true, minlength: true});
    form.markAsDirty();

    expect(optionBase.shouldDisplayErrors(form)).toEqual(true);
  });

  it('should `shouldDisplayErrors()` return false when form is not dirty', () => {
    form.setErrors({mismatched: true, minlength: true});

    expect(optionBase.shouldDisplayErrors(form)).toEqual(false);
  });


  it('should `shouldDisplayErrors()` return false when there is no errors', () => {
    form.markAsDirty();

    expect(optionBase.shouldDisplayErrors(form)).toEqual(false);
  });
});
