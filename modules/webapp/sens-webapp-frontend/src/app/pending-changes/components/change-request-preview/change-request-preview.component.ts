import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { PendingChange } from '@app/pending-changes/models/pending-changes';
import { ChangeRequestPreviewFormService } from '@app/pending-changes/services/change-request-preview-form.service';
import { Header } from '@app/ui-components/header/header';
import { environment } from '@env/environment';

@Component({
  selector: 'app-change-request-preview',
  templateUrl: './change-request-preview.component.html',
  styleUrls: ['./change-request-preview.component.scss'],
  providers: [ChangeRequestPreviewFormService],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ChangeRequestPreviewComponent {

  public pendingChangeData: PendingChange;
  public pendingChangeHeader: Header;

  @Input()
  public set pendingChange(value: PendingChange) {
    this.form.reset();
    this.pendingChangeHeader = {
      title: 'pendingChanges.changeRequestDetails.title',
      parameter: value.id.toString(),
    };
    this.pendingChangeData = value;
  }

  public get pendingChange(): PendingChange {
    return this.pendingChangeData;
  }

  form: FormGroup = this.changeRequestPreviewFormService.build();

  @Input()
  requiredCommentValidationMessage: string;
  translatePrefix = 'pendingChanges.changeRequestDetails.';

  dateFormatting = environment.dateFormatting;

  constructor(private changeRequestPreviewFormService: ChangeRequestPreviewFormService,
              private cdr: ChangeDetectorRef) { }

  public get comment(): string {
    return this.form.get('comment').value;
  }

  public checkValidity(): boolean {
    this.form.markAllAsTouched();
    this.cdr.markForCheck();
    return this.form.valid;
  }

}
