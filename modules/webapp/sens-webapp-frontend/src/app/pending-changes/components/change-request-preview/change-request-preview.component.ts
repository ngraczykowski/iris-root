import { Component, Input, OnInit } from '@angular/core';
import { environment } from '@env/environment';

@Component({
  selector: 'app-change-request-preview',
  templateUrl: './change-request-preview.component.html',
  styleUrls: ['./change-request-preview.component.scss']
})
export class ChangeRequestPreviewComponent implements OnInit {

  @Input() changeRequestData;
  @Input() changeRequestID;

  translatePrefix = 'pendingChanges.changeRequestDetails.';

  dateFormatting = environment.dateFormatting;

  constructor() { }

  ngOnInit() {
  }

}
