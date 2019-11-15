import { Component, EventEmitter } from '@angular/core';
import { DynamicComponent } from '@app/components/dynamic-view/dynamic-view.component';
import { OpenChangelogEvent } from '@app/templates/approver/approver.store';

@Component({
  selector: 'app-maker-comment-view',
  templateUrl: './comment-view.component.html',
  styleUrls: ['./comment-view.component.scss']
})
export class CommentViewComponent implements DynamicComponent {

  data: InputData;

  onReadMoreClick() {
    this.data.openChangelog.emit({
      decisionTreeId: this.data.decisionTreeId,
      matchGroupId: this.data.matchGroupId
    });
  }

}

interface InputData {
  decisionTreeId: number;
  matchGroupId: number;
  comment: string;
  userName: string;
  openChangelog: EventEmitter<OpenChangelogEvent>;
}
