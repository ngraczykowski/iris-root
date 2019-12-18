import { Component, Input, OnInit } from '@angular/core';
import { AuthenticatedUserFacade } from '@app/shared/auth/authenticated-user-facade.service';
import { Observable } from 'rxjs';
import { DynamicComponent } from '../../../../../components/dynamic-view/dynamic-view.component';
import { Authority } from '../../../../../shared/auth/principal.model';

export interface WorkflowDecisionTreeNameViewData {
  decisionTreeId: number;
  decisionTreeName: string;
}

@Component({
  selector: 'app-workflow-decision-tree-name-view',
  templateUrl: './workflow-decision-tree-name-view.component.html',
  styleUrls: ['./workflow-decision-tree-name-view.component.scss']
})
export class WorkflowDecisionTreeNameViewComponent implements DynamicComponent, OnInit {

  @Input()
  set data(data: WorkflowDecisionTreeNameViewData) {
    this.refreshData(data);
  }

  decisionTreeName: string;
  showLink: Observable<boolean>;
  link: string;

  constructor(private authenticatedUser: AuthenticatedUserFacade) { }

  ngOnInit() {
  }

  private refreshData(data: WorkflowDecisionTreeNameViewData) {
    this.showLink = this.authenticatedUser.hasAuthority(Authority.DECISION_TREE_LIST);
    this.link = `/decision-tree/${data.decisionTreeId}`;
    this.decisionTreeName = data.decisionTreeName;
  }

}
