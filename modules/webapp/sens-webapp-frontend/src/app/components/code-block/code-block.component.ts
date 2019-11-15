import { Component, ElementRef, Input, ViewChild } from '@angular/core';

@Component({
  selector: 'app-code-block',
  templateUrl: './code-block.component.html',
  styleUrls: ['./code-block.component.scss']
})
export class CodeBlockComponent {

  @Input() content: string;
  @Input() expandable: boolean;

  @ViewChild('codeBlockContent', {static: true}) contentCode: ElementRef;

  contentFullHeight: number;
  contentExpanded = false;

  constructor() { }

  shouldExpandCodeBlock() {
    return this.contentCode.nativeElement.offsetHeight > 80;
  }

  expandCodeBlock() {
    this.contentFullHeight = this.contentCode.nativeElement.offsetHeight;
    this.contentExpanded = true;
  }

  collapseCodeBlock() {
    this.contentFullHeight = null;
    this.contentExpanded = false;
  }
}
