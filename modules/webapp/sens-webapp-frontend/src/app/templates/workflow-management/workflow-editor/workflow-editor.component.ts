import { Component, OnDestroy, OnInit } from '@angular/core';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { ErrorMapper } from '@app/shared/http/error-mapper';
import { WorkflowDetails } from '@model/workflow.model';
import { TranslateService } from '@ngx-translate/core';
import { Observable, Subscription } from 'rxjs';
import { finalize, switchMap } from 'rxjs/operators';
import { WorkflowEditEvent, WorkflowService } from '../workflow-service/workflow.service';
import { WorkflowEditFormService } from './workflow-edit-form/workflow-edit-form.service';

@Component({
  selector: 'app-workflow-editor',
  templateUrl: './workflow-editor.component.html',
  styleUrls: ['./workflow-editor.component.scss']
})
export class WorkflowEditorComponent implements OnInit, OnDestroy {

  private readonly rolesDictPath = 'user-management.data.roles.';

  private readonly loadErrorMapper = new ErrorMapper(
      {}, 'workflowManagement.workflowEditor.error.load.');
  private readonly saveErrorMapper = new ErrorMapper({
    'UserRoleCheckFailedException': 'USER_ROLE_CHECK_FAILED',
    'InvalidMappingResultCountException': 'USER_NOT_FOUND',
    'ApprovalLevelHasChangeRequestsException': 'APPROVAL_LEVEL_HAS_CHANGE_REQUESTS'
  }, 'workflowManagement.workflowEditor.error.save.');

  loadErrorMessage: string;
  loadInProgress: boolean;

  saveErrorMessage: string;
  saveInProgress: boolean;

  show: boolean;

  workflow: WorkflowDetails;
  private loadSubscription: Subscription;
  private saveSubscription: Subscription;

  constructor(
      private workflowService: WorkflowService,
      private formService: WorkflowEditFormService,
      private eventService: LocalEventService,
      private translate: TranslateService
  ) {
    translate.setDefaultLang('en');
    translate.use('en');
  }

  ngOnInit() {
    this.workflowService.observeWorkflowEditEvents()
        .subscribe(event => this.onEdit(event));
  }

  ngOnDestroy() {
    this.cancelLoadTask();
    this.cancelSaveTask();
  }

  onClose() {
    this.resetErrorMessage();
    this.close();
  }

  onSave() {
    this.resetErrorMessage();
    this.saveWorkflow();
  }

  resetErrorMessage() {
    this.loadErrorMessage = null;
    this.saveErrorMessage = null;
  }

  shouldDisableSaveButton() {
    return !this.formService.isInitialized() || !this.formService.isDirty() || !this.formService.isValid();
  }

  private onEdit(event: WorkflowEditEvent) {
    this.workflow = null;
    this.show = true;
    this.loadWorkflow(event.workflow.decisionTreeId);
  }

  private loadWorkflow(decisionTreeId: number) {
    this.cancelLoadTask();
    this.loadInProgress = true;
    this.workflowService.getWorkflowDetails(decisionTreeId)
        .pipe(finalize(() => this.loadInProgress = false))
        .subscribe(
            data => this.onLoadSuccess(data),
            error => this.onLoadError(error)
        );
  }

  private cancelLoadTask() {
    if (this.loadSubscription) {
      this.loadSubscription.unsubscribe();
    }
  }

  private onLoadSuccess(data: WorkflowDetails) {
    this.loadErrorMessage = null;
    this.workflow = data;
  }

  private onLoadError(error: any) {
    this.loadErrorMessage = this.loadErrorMapper.get(error);
  }

  private saveWorkflow() {
    this.cancelSaveTask();
    this.saveInProgress = true;
    this.workflowService.saveWorkflow(this.formService.createWorkflowChange())
        .pipe(finalize(() => this.saveInProgress = false))
        .subscribe(
            () => this.onSaveSuccess(),
            error => this.onSaveError(error)
        );
  }

  private cancelSaveTask() {
    if (this.saveSubscription) {
      this.saveSubscription.unsubscribe();
    }
  }

  private onSaveSuccess() {
    this.saveErrorMessage = null;
    this.sendSuccessEvent();
    this.close();
  }

  private sendSuccessEvent() {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        type: 'success',
        message: 'workflowManagement.workflowEditor.notification.save.success'
      }
    });
  }


  private onSaveError(error: any) {
    const message = this.saveErrorMapper.get(error);
    if (error && error.error) {
      this.setErrorMessageBasedOnExceptionData(error, message);
    } else {
      this.saveErrorMessage = message;
    }
  }

  private setErrorMessageBasedOnExceptionData(error: any, message: string) {
    if (!error.error.extras) {
      this.saveErrorMessage = message;
      return;
    }

    if (error.error.key === 'UserRoleCheckFailedException') {
      this.getUserRoleCheckFailedMessage(error, message)
          .subscribe(msg => this.saveErrorMessage = msg);
    }

    if (error.error.key === 'InvalidMappingResultCountException') {
      this.getInvalidUserMappingResultMessage(error, message)
          .subscribe(msg => this.saveErrorMessage = msg);
    }

    if (error.error.key === 'ApprovalLevelHasChangeRequestsException') {
      this.getApprovalLevelHasChangeRequestsException(error, message)
          .subscribe(msg => this.saveErrorMessage = msg);
    }
  }

  private getUserRoleCheckFailedMessage(error: any, message: string): Observable<string> {
    const users = error.error.extras['users'];
    return this.translate.get(this.rolesDictPath + error.error.extras['role'])
        .pipe(switchMap(roleName =>
            this.translate.get(message, {users: users, role: roleName})));
  }

  private getInvalidUserMappingResultMessage(error: any, message: string): Observable<string> {
    const users = error.error.extras['users'];
    return this.translate.get(message, {users: users});
  }

  private getApprovalLevelHasChangeRequestsException(error: any, message: string): Observable<string> {
    const approvalLevels = error.error.extras['approvalLevels'];
    return this.translate.get(message, {approvalLevels: approvalLevels});
  }

  private close() {
    this.show = false;
  }
}
