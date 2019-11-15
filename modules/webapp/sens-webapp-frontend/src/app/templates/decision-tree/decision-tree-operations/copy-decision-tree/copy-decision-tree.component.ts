import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { finalize } from 'rxjs/operators';
import { EventKey } from '../../../../shared/event/event.service.model';
import { LocalEventService } from '../../../../shared/event/local-event.service';
import { ErrorMapper } from '../../../../shared/http/error-mapper';
import { CopyDecisionTreeService } from './copy-decision-tree.service';

export class DecisionTreeConfiguration {
  name;
}

@Component({
  selector: 'app-copy-decision-tree',
  templateUrl: './copy-decision-tree.component.html',
  styleUrls: ['./copy-decision-tree.component.scss']
})
export class CopyDecisionTreeComponent implements OnInit {

  private static ERROR_MAPPER = new ErrorMapper({
    'DecisionTreeNameAlreadyExists': 'NAME_ALREADY_EXISTS'
  }, 'decisionTree.message.copy.error.');

  @Output() copy: EventEmitter<any> = new EventEmitter();

  show;
  inProgress: boolean;
  dirty: { name: boolean };

  decisionTree: { id, name };
  config: DecisionTreeConfiguration;
  errorKey;

  constructor(private copyDecisionTreeService: CopyDecisionTreeService, private eventService: LocalEventService) { }

  ngOnInit() {
  }

  open(decisionTree: { id, name }) {
    this.decisionTree = decisionTree;
    this.config = new DecisionTreeConfiguration();
    this.errorKey = null;
    this.show = true;
    this.resetDirty();
  }

  close() {
    this.show = false;
  }

  confirm() {
    this.errorKey = null;
    this.inProgress = true;
    this.resetDirty();
    this.copyDecisionTreeService.copy(this.decisionTree.id, this.config.name)
        .pipe(finalize(() => this.inProgress = false))
        .subscribe(
            () => {
              this.copy.emit();
              this.close();
              this.sendNotificationSuccessEvent();
            },
            (err) => {
              this.errorKey = CopyDecisionTreeComponent.ERROR_MAPPER.get(err);
            });
  }

  shouldDisableConfirmButton() {
    return !this.anyPropertyIsDirty() || this.isNameTooLong() || this.isNameEmpty();
  }

  isNameTooLong() {
    return this.config.name && this.config.name.length > 64;
  }

  isNameEmpty() {
    return !this.config.name || !this.config.name.trim();
  }

  isNameServerError() {
    console.log(this.errorKey);
    return this.errorKey;
  }

  onChangeName() {
    this.dirty['name'] = true;
  }

  private resetDirty() {
    this.dirty = {
      'name': false
    };
  }

  private anyPropertyIsDirty() {
    return Object.keys(this.dirty).some(k => this.dirty[k]);
  }


  private sendNotificationSuccessEvent() {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        type: 'success',
        message: 'decisionTree.message.copy.success'
      }
    });
  }
}
