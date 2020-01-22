import { Component, Input, OnInit } from '@angular/core';
import { ClipboardService } from '@app/shared/clipboard/clipboard.service';
import { DecisionTreeDetails, DecisionTreePermission } from '../../../model/decision-tree.model';

@Component({
  selector: 'app-decision-tree-info',
  templateUrl: './decision-tree-info.component.html',
  styleUrls: ['./decision-tree-info.component.scss']
})
export class DecisionTreeInfoComponent implements OnInit {

  @Input()
  set decisionTreeDetails(decisionTreeDetails: DecisionTreeDetails) {
    // TODO(mmastylo): execute separate call for permissions
    this.hasDecisionTreeViewAccess = true;
    // this.hasDecisionTreeViewAccess =
    //     decisionTreeDetails.permissions.includes(DecisionTreePermission.DECISION_TREE_VIEW);
    this._decisionTreeDetails = decisionTreeDetails;
    this.activations = decisionTreeDetails.activations;
  }

  get decisionTreeDetails(): DecisionTreeDetails {
    return this._decisionTreeDetails;
  }

  hasDecisionTreeViewAccess: boolean;
  _decisionTreeDetails: DecisionTreeDetails;
  activations: string[];

  constructor(private clipboardService: ClipboardService) { }

  ngOnInit() {
  }

  onCopyToClipboard(text: string) {
    this.clipboardService.copy(text);
  }
}
