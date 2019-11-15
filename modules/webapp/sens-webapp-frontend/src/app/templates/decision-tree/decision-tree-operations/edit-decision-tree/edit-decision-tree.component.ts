import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { finalize } from 'rxjs/operators';
import { EventKey } from '../../../../shared/event/event.service.model';
import { LocalEventService } from '../../../../shared/event/local-event.service';
import { ErrorMapper } from '../../../../shared/http/error-mapper';
import { EditDecisionTreeService } from './edit-decision-tree.service';

export class DecisionTreeSettings {
  name: string;
}

export class DecisionTreeConfiguration {
  name;
}

@Component({
  selector: 'app-edit-decision-tree',
  templateUrl: './edit-decision-tree.component.html',
  styleUrls: ['./edit-decision-tree.component.scss']
})
export class EditDecisionTreeComponent implements OnInit {

  private static ERROR_MAPPER = new ErrorMapper({
    'DecisionTreeNameAlreadyExists': 'name.NAME_ALREADY_EXISTS',
    'AssignmentAlreadyUsed': 'assignment.ASSIGNMENT_ALREADY_USED'
  }, 'decisionTree.message.edit.error.');

  @Output() edit: EventEmitter<any> = new EventEmitter();

  show;
  inProgress: boolean;
  dirty: { name: boolean };

  id;
  config: DecisionTreeConfiguration;

  private errorKey: string;

  constructor(private editDecisionTreeService: EditDecisionTreeService, private eventService: LocalEventService) { }

  ngOnInit() {
  }

  close() {
    this.show = false;
  }

  open(decisionTree: { id, name }) {
    this.id = decisionTree.id;
    this.errorKey = null;
    this.show = true;
    this.resetDirty();
    this.config = new DecisionTreeConfiguration();
    this.config.name = decisionTree.name;
  }

  isNameTooLong() {
    return this.config.name && this.config.name.length > 64;
  }

  isNameEmpty() {
    return !this.config.name || !this.config.name.trim();
  }

  isNameServerError() {
    return this.errorKey && this.errorKey.startsWith('decisionTree.message.edit.error.name');
  }

  isActivationServerError() {
    return this.errorKey && this.errorKey.startsWith('decisionTree.message.edit.error.active');
  }

  isAssignmentServerError() {
    return this.errorKey && this.errorKey.startsWith('decisionTree.message.edit.error.assignment');
  }

  isOtherServerError() {
    return this.errorKey && !this.isNameServerError() && !this.isActivationServerError() && !this.isAssignmentServerError();
  }

  shouldDisableConfirmButton() {
    return !this.anyPropertyIsDirty() || this.isNameTooLong() || this.isNameEmpty();
  }

  onChangeName() {
    this.dirty['name'] = true;
  }

  confirm() {
    this.errorKey = null;
    this.inProgress = true;
    this.editDecisionTreeService.patchDecisionTree(this.id, this.editSettings())
        .pipe(finalize(() => {
          this.resetDirty();
          this.inProgress = false;
        }))
        .subscribe(
            () => {
              this.edit.emit();
              this.close();
              this.sendNotificationSuccessEvent();
            },
            (err) => {
              this.errorKey = EditDecisionTreeComponent.ERROR_MAPPER.get(err);
            });
  }

  editSettings(): DecisionTreeSettings {
    const settings = new DecisionTreeSettings();
    settings.name = this.config.name;
    return settings;
  }

  private resetDirty() {
    this.dirty = {
      'name': false,
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
        message: 'decisionTree.message.edit.success'
      }
    });
  }
}
