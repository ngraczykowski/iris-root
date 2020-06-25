import { Injectable } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Injectable()
export class ChangeRequestPreviewFormService {

  private static formModel = {
    comment: [null, Validators.required]
  };

  private form: FormGroup;

  constructor(private formBuilder: FormBuilder) { }

  public build(): FormGroup {
    return this.form = this.formBuilder.group(ChangeRequestPreviewFormService.formModel);
  }

}
