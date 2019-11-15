import { Component, Input } from '@angular/core';
import { ClipboardService } from '@app/shared/clipboard/clipboard.service';
import { DecisionTreeFeatures } from '@model/decision-tree.model';

@Component({
  selector: 'app-decision-tree-info-features-list',
  templateUrl: './decision-tree-info-features-list.component.html',
  styleUrls: ['./decision-tree-info-features-list.component.scss']
})
export class DecisionTreeInfoFeaturesListComponent {

  constructor(private clipboardService: ClipboardService) { }

  @Input() features: DecisionTreeFeatures[];

  @Input() hasDecisionTreeViewAccess: boolean;

  onCopyToClipboard(text: string) {
    this.clipboardService.copy('f_' +  text);
  }
}
