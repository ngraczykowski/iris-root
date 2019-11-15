import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { finalize, map } from 'rxjs/operators';
import { EventKey } from '../../../../shared/event/event.service.model';
import { LocalEventService } from '../../../../shared/event/local-event.service';
import { ErrorMapper } from '../../../../shared/http/error-mapper';
import { DecisionTreeSettings, DropdownSelector, Option } from './create-new-decision-tree.model';
import { CreateNewDecisionTreeService } from './create-new-decision-tree.service';

// TODO(iwnek) refactor and create some generic validator class

export class DecisionTreeConfiguration {
  name;
  modelSelector: DropdownSelector;
}

@Component({
  selector: 'app-create-new-decision-tree',
  templateUrl: './create-new-decision-tree.component.html',
  styleUrls: ['./create-new-decision-tree.component.scss']
})
export class CreateNewDecisionTreeComponent implements OnInit {

  private static ERROR_MAPPER = new ErrorMapper({
    'DecisionTreeNameAlreadyExists': 'name.NAME_ALREADY_EXISTS',
  }, 'decisionTree.message.create.error.');

  @Output() create: EventEmitter<any> = new EventEmitter();

  show: boolean;
  inProgress: boolean;
  dirty: { name: boolean, model: boolean };

  optionSelected = null;
  selectedModel = false;
  config: DecisionTreeConfiguration;
  errorNameExist = false;

  private errorKey: string;

  constructor(private service: CreateNewDecisionTreeService, private eventService: LocalEventService) { }

  ngOnInit() {
    this.resetDirty();
  }

  open() {
    this.reset();
    this.show = true;
    this.config = new DecisionTreeConfiguration();
    this.service.getAvailableModels()
        .pipe(map(data => data.map(m => <Option>{id: m.id, name: m.name, selected: false})))
        .subscribe(data => this.config.modelSelector = new DropdownSelector(data));
  }

  reset() {
    this.optionSelected = null;
    this.selectedModel = false;
    this.inProgress = false;
    this.errorKey = null;
    this.resetDirty();
  }

  close() {
    this.show = false;
  }

  isNameTooLong() {
    return this.config.name && this.config.name.length > 64;
  }

  isNameEmpty() {
    return !this.config.name || !this.config.name.trim();
  }

  isNameServerError() {
    return this.errorKey && this.errorKey.startsWith('decisionTree.message.create.error.name');
  }

  hasEmptyModel() {
    return !this.config.modelSelector.getSelected();
  }

  isModelServerError() {
    return this.errorKey && this.errorKey.startsWith('decisionTree.message.create.error.model');
  }

  isOtherServerError() {
    return this.errorKey && !this.isNameServerError() && !this.isModelServerError();
  }

  shouldDisableConfirmButton() {
    return !this.anyPropertyIsDirty() || this.isNameTooLong() || this.isNameEmpty() || this.hasEmptyModel();
  }

  onChangeName() {
    this.dirty['name'] = true;
  }

  onChangeModel(option: Option) {
    this.config.modelSelector.select(option);
    this.dirty['model'] = true;
    this.selectedModel = true;
  }

  confirm() {
    this.errorKey = null;
    this.inProgress = true;
    this.service.createNewDecisionTree(this.createSettings())
        .pipe(finalize(() => {
          this.reset();
        }))
        .subscribe(
            () => {
              this.create.emit();
              this.close();
              this.sendNotificationSuccessEvent();
            },
            (err) => {
              this.errorNameExist = true;
            });
  }

  createSettings(): DecisionTreeSettings {
    const settings = new DecisionTreeSettings();
    settings.name = this.config.name;
    settings.modelId = this.config.modelSelector.getSelected().id;
    return settings;
  }

  private resetDirty() {
    this.dirty = {
      'name': false,
      'model': false
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
        message: 'decisionTree.message.create.success'
      }
    });
  }
}
