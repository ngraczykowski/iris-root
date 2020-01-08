import { Component, Input, OnInit } from '@angular/core';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';
import { Observable } from 'rxjs';
import { CellViewFactory } from '../../../../../../components/dynamic-view-table/table-data-mapper';
import { View } from '../../../../../../components/dynamic-view/dynamic-view.component';
import { Authority } from '../../../../../../shared/security/principal.model';
import { DecisionTree } from '../../../../../model/decision-tree.model';

export interface DecisionTreeNameViewData {
  decisionTreeId: number;
  decisionTreeName: string;
}

@Component({
  selector: 'app-decision-tree-name-view',
  templateUrl: './decision-tree-name-view.component.html',
  styleUrls: ['./decision-tree-name-view.component.scss']
})
export class DecisionTreeNameViewComponent implements OnInit {

  @Input()
  set data(data: DecisionTreeNameViewData) {
    this.link = `/decision-tree/${data.decisionTreeId}`;
    this.decisionTreeName = data.decisionTreeName;
  }

  decisionTreeName: string;
  showLink: Observable<boolean>;
  link: string;

  constructor(private authenticatedUser: AuthenticatedUserFacade) { }

  ngOnInit() {
    this.showLink = this.authenticatedUser.hasAuthority(Authority.DECISION_TREE_LIST);
  }
}

export class DecisionTreeNameCellViewFactory implements CellViewFactory<DecisionTree> {
  create(entry: DecisionTree): View {
    return {
      component: DecisionTreeNameViewComponent,
      data: <DecisionTreeNameViewData> {
        decisionTreeId: entry.id,
        decisionTreeName: entry.name
      }
    };
  }
}
