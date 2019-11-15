import { Component, Input, OnInit } from '@angular/core';

export interface ElementsList {
  label: string;
  url?: string;
}

export interface OptionsList {
  label: string;
  url: string;
}

export interface HintFeedbackInput {
  type: 'negative' | 'positive' | 'warning' | 'info';
  title?: string;
  descriptionPrimary?: string;
  elementsList?: ElementsList[];
  descriptionSecondary?: string;
  options?: OptionsList[];
  visible: boolean;
}

@Component({
  selector: 'app-hint-feedback',
  templateUrl: './hint-feedback.component.html',
  styleUrls: ['./hint-feedback.component.scss']
})

export class HintFeedbackComponent implements OnInit {

@Input() settings: HintFeedbackInput;

  constructor() { }

  hintType: string;

  ngOnInit() {
    this.setHintStatus();
  }

  setHintStatus() {
    const stylePrefix = 'hint-feedback-status-';
    this.hintType = stylePrefix + this.settings.type;
  }
}
