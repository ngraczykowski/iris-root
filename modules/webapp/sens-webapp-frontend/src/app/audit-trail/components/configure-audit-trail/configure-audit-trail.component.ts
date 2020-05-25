import { Component, EventEmitter, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import * as moment from 'moment';

@Component({
  selector: 'app-configure-audit-trail',
  templateUrl: './configure-audit-trail.component.html'
})
export class ConfigureAuditTrailComponent {
  @Output() formSubmit = new EventEmitter();

  form = new FormGroup({
    startDate: new FormControl('', Validators.required),
    endDate: new FormControl('', Validators.required)
  });

  constructor() { }

  onFormSubmit() {
    const startDate = moment(this.form.value.startDate).hours(0).minutes(0).seconds(0).toISOString(false);
    const endDate = moment(this.form.value.endDate).hours(23).minutes(59).seconds(59).toISOString(false);
    this.formSubmit.emit({
      startDate: startDate,
      endDate: endDate
    });
  }

  getTodayDate() {
    return new Date();
  }
}
