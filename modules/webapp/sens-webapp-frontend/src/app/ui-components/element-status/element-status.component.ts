import { Component, Input, OnChanges } from '@angular/core';

@Component({
  selector: 'app-element-status',
  templateUrl: './element-status.component.html',
  styleUrls: ['./element-status.component.scss']
})
export class ElementStatusComponent implements OnChanges {

  @Input() elementStatus;

  appearance: string;
  label: string;

  constructor() { }

  ngOnChanges () {
    this.convertStatus(this.elementStatus);
  }

  convertStatus(status) {
    const statusTranslatePrefix = 'aiStatus.';
    const stylePrefix = 'element-status--';
    const state = {
      active: 'active',
      disabled: 'disabled'
    };

    if (status) {
      this.appearance = stylePrefix + state.active;
      this.label = statusTranslatePrefix + state.active;
    } else {
      this.appearance = stylePrefix + state.disabled;
      this.label = statusTranslatePrefix + state.disabled;
    }
  }

}
