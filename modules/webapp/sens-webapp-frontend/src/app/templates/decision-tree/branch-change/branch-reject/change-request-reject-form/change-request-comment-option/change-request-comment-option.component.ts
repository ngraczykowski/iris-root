import { Component, Input } from '@angular/core';
import { AbstractControl } from '@angular/forms';

@Component({
  selector: 'app-change-request-comment-option',
  templateUrl: './change-request-comment-option.component.html',
  styleUrls: ['./change-request-comment-option.component.scss']
})
export class ChangeRequestCommentOptionComponent {

  @Input() control: AbstractControl;

  updateComment(comment) {
    this.control.setValue(comment);
  }
}
