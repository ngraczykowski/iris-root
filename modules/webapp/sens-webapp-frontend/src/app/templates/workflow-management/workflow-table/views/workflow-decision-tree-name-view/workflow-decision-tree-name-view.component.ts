import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '../../../../../components/dynamic-view/dynamic-view.component';
import { AuthService } from '../../../../../shared/auth/auth.service';
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
  showLink: boolean;
  link: string;

  constructor(private authService: AuthService) { }

  ngOnInit() {
  }

  private refreshData(data: WorkflowDecisionTreeNameViewData) {
    this.showLink = this.authService.hasAuthority(Authority.DECISION_TREE_LIST);
    this.link = `/decision-tree/${data.decisionTreeId}`;
    this.decisionTreeName = data.decisionTreeName;
  }

}
