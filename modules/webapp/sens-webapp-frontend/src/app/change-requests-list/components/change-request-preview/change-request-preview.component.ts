import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-change-request-preview',
  templateUrl: './change-request-preview.component.html',
  styleUrls: ['./change-request-preview.component.scss']
})
export class ChangeRequestPreviewComponent implements OnInit {

  @Input() changeRequestData;
  @Input() changeRequestID;

  translatePrefix = 'changeRequestsList.changeRequestDetails.';

  constructor() { }

  ngOnInit() {
  }

}
