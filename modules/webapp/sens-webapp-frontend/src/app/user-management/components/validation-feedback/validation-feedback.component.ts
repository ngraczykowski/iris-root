import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-validation-feedback',
  templateUrl: './validation-feedback.component.html',
  styleUrls: ['./validation-feedback.component.scss']
})
export class ValidationFeedbackComponent implements OnInit {

  @Input() content: string;
  @Input() active: boolean;
  @Input() type: string;

  constructor() { }

  ngOnInit() {
  }

  iconName() {
    switch (this.type) {
      case 'positive': {
        return 'check-mark';
      }
      case 'negative': {
        return 'close';
      }
    }
  }

}
