import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-preview-element',
  templateUrl: './preview-element.component.html',
  styleUrls: ['./preview-element.component.scss']
})
export class PreviewElementComponent implements OnInit {

  @Input() status: boolean;
  @Input() previewData: string[];

  @Output() closePreview: EventEmitter<any> = new EventEmitter();

  constructor() { }

  ngOnInit() {
  }

  onClose() {
    this.closePreview.emit(false);
  }

}
