import { Component, OnInit } from '@angular/core';
import { AbstractControl } from '@angular/forms';
import { ErrorData } from '@app/components/error-feedback/error-feedback.component';
import { ChangeRequestRejectFormService } from '@app/templates/decision-tree/branch-change/branch-reject/change-request-reject-form/change-request-reject-form.service';

@Component({
  selector: 'app-change-request-reject-form',
  templateUrl: './change-request-reject-form.component.html',
  styleUrls: ['./change-request-reject-form.component.scss']
})
export class ChangeRequestRejectFormComponent implements OnInit {

  get commentControl(): AbstractControl {
    return this.formService.getCommentControl();
  }

  get errors(): ErrorData[] {
    return this.formService.getFormErrorKeys().map(k => <ErrorData> {key: k});
  }

  constructor(private formService: ChangeRequestRejectFormService) { }

  ngOnInit() {
    this.formService.init();
  }

  isInitialized() {
    return this.formService.isInitialized();
  }
}
