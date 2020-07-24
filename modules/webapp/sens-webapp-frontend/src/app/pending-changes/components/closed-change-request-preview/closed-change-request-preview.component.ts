import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { PendingChange } from '@app/pending-changes/models/pending-changes';
import { Header } from '@app/ui-components/header/header';
import { environment } from '@env/environment';

@Component({
  selector: 'app-closed-change-request-preview',
  templateUrl: './closed-change-request-preview.component.html',
  styleUrls: ['./closed-change-request-preview.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ClosedChangeRequestPreviewComponent {

  public pendingChangeData: PendingChange;
  public pendingChangeHeader: Header;

  @Input()
  public set pendingChange(value: PendingChange) {
    this.pendingChangeHeader = {
      title: 'pendingChanges.changeRequestDetails.title',
      parameter: value.id.toString(),
    };
    this.pendingChangeData = value;
  }

  public get pendingChange(): PendingChange {
    return this.pendingChangeData;
  }

  translatePrefix = 'pendingChanges.changeRequestDetails.';

  dateFormatting = environment.dateFormatting;

  constructor() { }

}
