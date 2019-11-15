import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '../../../../../components/dynamic-view/dynamic-view.component';
import { UserCellViewData } from '../user-view-factories';
import { HighlightSearchPipe } from '@app/templates/user-management/highlight-search.pipe';

export interface UserNameViewData extends UserCellViewData {
  userName: string;
  filterQuery?: string;
}

@Component({
  selector: 'app-text-view',
  templateUrl: './user-name-view.component.html',
  styleUrls: ['./user-name-view.component.scss'],
  providers: [HighlightSearchPipe]
})
export class UserNameViewComponent implements DynamicComponent, OnInit {

  @Input() data: UserNameViewData;

  constructor() { }

  ngOnInit() {
  }

}
