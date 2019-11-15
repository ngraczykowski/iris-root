import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Translator } from '@app/shared/translate/translator';
import { TranslateService } from '@ngx-translate/core';
import { from, Observable, Subscription } from 'rxjs';
import { concatMap, finalize, map, toArray } from 'rxjs/operators';
import { AssignmentDto, AssignmentView, User, UserType } from '../../../../model/user.model';
import { EditUserTemplate } from '../edit-user.model';
import { EditUserTemplateFactory } from './factories/edit-user-profile-template-factory';
import { EditUserTemplateFormFactory } from './factories/edit-user-profile-template-form-factory';


@Component({
  selector: 'app-edit-user-template',
  templateUrl: './edit-user-template.component.html',
  styleUrls: ['./edit-user-template.component.scss']
})
export class EditUserTemplateComponent implements OnInit, OnDestroy {

  userName: String;
  displayName: String;
  withPassword: boolean;
  assignments: AssignmentView[];
  form: FormGroup;
  translator: Translator;
  private readonly assignedDictPath = 'user-management.permissions.assigned.';
  private assignmentsSubscription: Subscription;
  private inProgress: boolean;

  private static getAssignmentView(
      translatedRole: string, decisionTreeName: string): AssignmentView {

    return {decisionTreeName: decisionTreeName, role: translatedRole};
  }

  constructor(translateService: TranslateService) {
    this.translator = new Translator(translateService);
  }

  ngOnInit() {
  }

  load(user: User) {
    this.inProgress = true;
    this.cancelAssignmentsSubscription();
    this.userName = user.userName;
    this.displayName = user.displayName;
    this.withPassword = user.type === UserType.INTERNAL;
    this.assignmentsSubscription = this.loadAssignments(user.assignmentViews)
        .pipe(finalize(() => this.inProgress = false))
        .subscribe(assignments => this.assignments = assignments);
    this.form = EditUserTemplateFormFactory.create(user, this.withPassword);
  }

  private loadAssignments(assignments: AssignmentDto[]): Observable<AssignmentView[]> {
    return from(assignments ? assignments : [])
        .pipe(
            concatMap(a => this.getAssignmentsView(a)),
            toArray()
        );
  }

  private getAssignmentsView(assignment: AssignmentDto): Observable<AssignmentView> {
    return this.translator.translateWithParam(
        assignment.role, this.assignedDictPath, {level: assignment.level})
        .pipe(
            map(translatedRole => EditUserTemplateComponent.getAssignmentView(
                translatedRole, assignment.decisionTreeName))
        );
  }

  isReady() {
    return this.form && this.form.valid && this.form.dirty && !this.inProgress;
  }

  getTemplate(): EditUserTemplate {
    return EditUserTemplateFactory.create(this.form, this.withPassword);
  }

  ngOnDestroy() {
    this.cancelAssignmentsSubscription();
  }

  cancelAssignmentsSubscription() {
    if (this.assignmentsSubscription) {
      this.assignmentsSubscription.unsubscribe();
    }
  }
}
