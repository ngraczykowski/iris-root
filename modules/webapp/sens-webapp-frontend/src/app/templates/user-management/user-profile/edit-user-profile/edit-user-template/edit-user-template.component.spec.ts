import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormGroup } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { User, UserType } from '@model/user.model';
import { TranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';
import { EditUserTemplate } from '../edit-user.model';
import { EditUserTemplateComponent } from './edit-user-template.component';
import { EditUserTemplateModule } from './edit-user-template.module';
import { EditUserTemplateFactory } from './factories/edit-user-profile-template-factory';
import { EditUserTemplateFormFactory } from './factories/edit-user-profile-template-form-factory';

describe('EditUserProfileFormComponent', () => {
  let component: EditUserTemplateComponent;
  let fixture: ComponentFixture<EditUserTemplateComponent>;
  let translateService: TranslateService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [EditUserTemplateModule, RouterTestingModule, HttpClientTestingModule]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    translateService = TestBed.get(TranslateService);
    fixture = TestBed.createComponent(EditUserTemplateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set withPassword to true if load internal user', () => {
    spyOn(EditUserTemplateFormFactory, 'create');

    component.load(<User> {type: UserType.INTERNAL, assignments: []});

    expect(component.withPassword).toBeTruthy();
  });

  it('should set withPassword to false if load external user', () => {
    spyOn(EditUserTemplateFormFactory, 'create');

    component.load(<User> {type: UserType.EXTERNAL, assignments: []});

    expect(component.withPassword).toBeFalsy();
  });

  it('should set form created by factory after load user', () => {
    const form = new FormGroup({});
    const user = <User> {type: UserType.INTERNAL, assignments: []};
    spyOn(EditUserTemplateFormFactory, 'create').and.callFake((u, withPassword) => {
      if (u === user && withPassword === component.withPassword) {
        return form;
      }
      return null;
    });

    component.load(user);

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
    const template = <EditUserTemplate> {};
    component.form = new FormGroup({});
    component.withPassword = true;
    spyOn(EditUserTemplateFactory, 'create').and.callFake((f, withPassword) => {
      if (f === component.form && withPassword === component.withPassword) {
        return template;
      }
      return null;
    });

    expect(component.getTemplate()).toBe(template);
  });

  it('should return valid user assignations', () => {
    const TRANSLATED_ROLE = 'Approver level 1';
    const DECISION_TREE_NAME = 'Decision tree';

    const userToLoad = <User> {
      assignmentViews: [{
        decisionTreeId: '1',
        decisionTreeName: DECISION_TREE_NAME,
        role: 'ROLE_APPROVER',
        level: 1
      }],
      roles: ['ROLE_APPROVER']
    };
    spyOn(translateService, 'get')
        .and
        .returnValue(of(TRANSLATED_ROLE))
        .withArgs('ROLE_APPROVER', 1);

    component.load(userToLoad);

    expect(component.assignments).toEqual([{
      decisionTreeName: DECISION_TREE_NAME,
      role: TRANSLATED_ROLE
    }]);
  });
});
