import { Component, Input, OnInit } from '@angular/core';
import { Change } from '@app/components/changelog/changelog.model';

@Component({
  selector: 'app-status-change',
  templateUrl: './status-change.component.html',
  styleUrls: ['./status-change.component.scss']
})
export class StatusChangeComponent implements OnInit {

  @Input() statusChange: Change<boolean>;

  constructor() { }

  ngOnInit() {
  }

}
