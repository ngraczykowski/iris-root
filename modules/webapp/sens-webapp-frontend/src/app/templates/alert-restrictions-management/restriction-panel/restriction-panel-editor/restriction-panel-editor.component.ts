import { HttpClient } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { HintFeedbackInput } from '@app/components/hint-feedback/hint-feedback.component';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { AlertRestrictionsManagementService } from '@app/templates/alert-restrictions-management/alert-restrictions-management.service';
import { TranslateService } from '@ngx-translate/core';

export interface RestrictionData {
  name: string;
  units: string[];
  countries: string[];
  userIds: string[];
}

@Component({
  selector: 'app-restriction-panel-editor',
  templateUrl: './restriction-panel-editor.component.html',
  styleUrls: ['./restriction-panel-editor.component.scss']
})
export class RestrictionPanelEditorComponent implements OnInit {

  constructor(private httpClient: HttpClient,
              private alertRestrictionsService: AlertRestrictionsManagementService,
              private eventService: LocalEventService,
              private translate: TranslateService) { }

  @Input() restrictionId;

  restrictionDataInput: RestrictionData;

  restrictionName = '';
  restrictionCountries = '';
  restrictionUnits = '';
  private restrictionUserIds = [];

  translatePrefix = 'alertRestrictionsManagement.modal.';
  translateSectionPrefix = this.translatePrefix + 'content.';

  apiURL = '/api/restriction/';

  restrictionNameError: string[] = [];

  status = false;

  error: HintFeedbackInput = {
    type: 'negative',
    visible: false,
  };

  @Output() someEvent = new EventEmitter();

  ngOnInit() {
    if (this.restrictionId) {
      this.getRestrictionData();
    } else {
      this.status = true;
    }
  }

  private getRestrictionData() {
    this.httpClient.get(this.apiURL + this.restrictionId)
        .subscribe((data: RestrictionData) => {
              this.restrictionDataInput = data;
              this.restrictionName = data.name;
              this.restrictionCountries = this.convertArrayToString(data.countries);
              this.restrictionUnits = this.convertArrayToString(data.units);
              this.restrictionUserIds = data.userIds;
              this.status = true;
            },
            error => {
              console.log('Error');
            });
  }

  hasRestrictionName() {
    const emptyField = this.translateSectionPrefix + 'sectionName.input.errors.empty';
    if (this.restrictionName === '') {
      this.addInputError(emptyField);
    } else {
      this.removeInputError(emptyField);
    }
  }

  updateUsersList(usersList) {
    this.restrictionUserIds = [];
    if (usersList) {
      for (const assignedUser of usersList) {
        this.restrictionUserIds.push(assignedUser);
      }
    }
  }

  private addInputError(error) {
    if (this.restrictionNameError.indexOf(error)) {
      this.restrictionNameError.push(error);
    }
  }

  private removeInputError(error) {
    this.restrictionNameError.splice(this.restrictionNameError.indexOf(error), 1);
  }

  shouldDisableActionButton() {
    if (this.restrictionId) {
      if (this.status === true) {
        return this.editRestrictionValidation();
      } else {
        return true;
      }
    } else {
      return this.newRestrictionValidation();
    }
  }

  private editRestrictionValidation() {
    return this.restrictionName === this.restrictionDataInput.name &&
        this.restrictionUnits === this.convertArrayToString(this.restrictionDataInput.units) &&
        this.restrictionCountries === this.convertArrayToString(this.restrictionDataInput.countries) &&
        this.restrictionUserIds === this.restrictionDataInput.userIds ||
        this.restrictionName === '';
  }

  private newRestrictionValidation() {
    return this.restrictionName === '' ||
        this.restrictionName === '' &&
        this.restrictionUnits === '' &&
        this.restrictionCountries === '' &&
        this.restrictionUserIds.length === 0;
  }

  closePanel() {
    this.alertRestrictionsService.restrictionPanelData({'status': false});
  }

  addRestriction() {
    this.httpClient.put(this.apiURL,
        {
          'name': this.restrictionName,
          'units': this.convertStringToArray(this.restrictionUnits),
          'countries': this.convertStringToArray(this.restrictionCountries),
          'userIds': this.restrictionUserIds,
        })
        .subscribe(
            data => {
              this.refreshTableData();
              this.closePanel();
              this.confirmOperation(this.translatePrefix + 'confirmation.added');
            },
            error => {
              this.showError(
                  this.translate.instant(this.translatePrefix + 'error.notAdded.title'),
                  this.translate.instant(this.translatePrefix + 'error.notAdded.description'));
            }
        );

    this.refreshTableData();
  }

  saveRestriction() {
    this.httpClient.post(this.apiURL + this.restrictionId,
        {
          'name': this.restrictionName,
          'units': this.convertStringToArray(this.restrictionUnits),
          'countries': this.convertStringToArray(this.restrictionCountries),
          'userIds': this.restrictionUserIds,
        })
        .subscribe(
            data => {
              this.refreshTableData();
              this.closePanel();
              this.confirmOperation(this.translatePrefix + 'confirmation.edited');
            },
            error => {
              this.showError(
                  this.translate.instant(this.translatePrefix + 'error.notSaved.title'),
                  this.translate.instant(this.translatePrefix + 'error.notSaved.description'));
            }
        );
    this.refreshTableData();
  }

  private confirmOperation(message) {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        type: 'success',
        message: message
      }
    });
  }

  private showError(title, description) {
    this.error.type = 'negative';
    this.error.title = title;
    this.error.descriptionPrimary = description;
    this.error.visible = true;
  }

  private refreshTableData() {
    this.alertRestrictionsService.get();
  }

  private convertStringToArray(inputString) {
    inputString = inputString.replace(/\s/g, '');
    return inputString.split(',');
  }

  private convertArrayToString(inputArray) {
    return inputArray.join(', ');
  }
}
