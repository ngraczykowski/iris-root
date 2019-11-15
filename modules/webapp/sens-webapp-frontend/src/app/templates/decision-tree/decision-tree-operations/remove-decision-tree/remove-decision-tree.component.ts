import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { EventKey } from '../../../../shared/event/event.service.model';
import { LocalEventService } from '../../../../shared/event/local-event.service';
import { ErrorMapper } from '../../../../shared/http/error-mapper';
import { RemoveDecisionTreeService } from './remove-decision-tree.service';

@Component({
  selector: 'app-remove-decision-tree',
  templateUrl: './remove-decision-tree.component.html',
  styleUrls: ['./remove-decision-tree.component.scss']
})
export class RemoveDecisionTreeComponent implements OnInit {

  private static ERROR_MAPPER = new ErrorMapper({}, 'decisionTree.message.remove.error.');

  @Output() remove: EventEmitter<any> = new EventEmitter();

  show;
  inProgress: boolean;

  decisionTree: { id, name };
  confirmInputValue;

  constructor(private removeDecisionTreeService: RemoveDecisionTreeService, private eventService: LocalEventService) { }

  ngOnInit() {
  }

  open(decisionTree: { id, name }) {
    this.decisionTree = decisionTree;
    this.confirmInputValue = null;
    this.show = true;
  }

  close() {
    this.show = false;
  }

  confirm() {
    this.removeDecisionTreeService.remove(this.decisionTree.id).subscribe(
        () => {
          this.remove.emit();
          this.close();
          this.sendNotificationSuccessEvent();
        },
        (err) => {
          const errorMessage = RemoveDecisionTreeComponent.ERROR_MAPPER.get(err);
          this.sendNotificationErrorEvent(errorMessage);
        });
  }

  shouldDisableConfirmButton() {
    return this.confirmInputValue !== 'Delete';
  }

  private sendNotificationSuccessEvent() {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        type: 'success',
        message: 'decisionTree.message.remove.success'
      }
    });
  }

  private sendNotificationErrorEvent(errorMessage) {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        type: 'error',
        message: errorMessage
      }
    });
  }
}
