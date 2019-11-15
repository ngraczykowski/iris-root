import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { UserType } from '../../../../model/user.model';
import { NewUserTemplate } from '../new-user.model';
import { NewUserTemplateFactory } from './factories/new-user-profile-template-factory';
import { NewUserTemplateFormFactory } from './factories/new-user-profile-template-form-factory';

@Component({
  selector: 'app-new-user-template',
  templateUrl: './new-user-template.component.html',
  styleUrls: ['./new-user-template.component.scss']
})
export class NewUserTemplateComponent implements OnInit {

  withPassword: boolean;
  form: FormGroup;

  constructor() { }

  ngOnInit() {
  }

  load(userType: UserType) {
    this.withPassword = userType === UserType.INTERNAL;
    this.form = NewUserTemplateFormFactory.create(this.withPassword);
  }

  isReady() {
    return this.form && this.form.valid && this.form.dirty;
  }

  getTemplate(): NewUserTemplate {
    return NewUserTemplateFactory.create(this.form, this.withPassword);
  }
}
