import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormGroup } from '@angular/forms';
import { UserType } from '../../../../model/user.model';
import { NewUserTemplate } from '../new-user.model';
import { NewUserTemplateFactory } from './factories/new-user-profile-template-factory';
import { NewUserTemplateFormFactory } from './factories/new-user-profile-template-form-factory';

import { NewUserTemplateComponent } from './new-user-template.component';
import { NewUserTemplateModule } from './new-user-template.module';

describe('NewUserTemplateComponent', () => {
  let component: NewUserTemplateComponent;
  let fixture: ComponentFixture<NewUserTemplateComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [NewUserTemplateModule]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewUserTemplateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set withPassword to true if load with internal type', () => {
    spyOn(NewUserTemplateFormFactory, 'create');

    component.load(UserType.INTERNAL);

    expect(component.withPassword).toBeTruthy();
  });

  it('should set withPassword to false if load with external type', () => {
    spyOn(NewUserTemplateFormFactory, 'create');

    component.load(UserType.EXTERNAL);

    expect(component.withPassword).toBeFalsy();
  });

  it('should set form created by factory after load user', () => {
    const form = new FormGroup({});
    spyOn(NewUserTemplateFormFactory, 'create').and.callFake((withPassword) => {
      if (withPassword === component.withPassword) {
        return form;
      }
      return null;
    });

    component.load(UserType.INTERNAL);

    expect(component.form).toBe(form);
  });

  it('should isReady return false if form is not loaded', () => {
    expect(component.isReady()).toBeFalsy();
  });

  it('should isReady return false if form is not dirty', () => {
    component.form = new FormGroup({});
    component.form.setErrors({error: true});

    expect(component.isReady()).toBeFalsy();
  });

  it('should isReady return true if form has no errors and is dirty', () => {
    component.form = new FormGroup({});
    component.form.markAsDirty();
    component.form.setErrors(null);

    expect(component.isReady()).toBeTruthy();
  });

  it('should getTemplate return valid template', () => {
    const template = <NewUserTemplate> {};
    component.form = new FormGroup({});
    component.withPassword = true;
    spyOn(NewUserTemplateFactory, 'create').and.callFake((f, withPassword) => {
      if (f === component.form && withPassword === component.withPassword) {
        return template;
      }
      return null;
    });

    expect(component.getTemplate()).toBe(template);
  });
});
