import { Input } from '@angular/core';
import { TableData } from '@app/components/dynamic-view-table/dynamic-view-table.component';
import {
  TableDataMapper,
  TableDataMapperConfiguration
} from '@app/components/dynamic-view-table/table-data-mapper';
import { DecisionTree } from '@app/templates/model/decision-tree.model';

export abstract class DecisionTreeTableBaseComponent {

  @Input()
  set decisionTrees(decisionTrees: DecisionTree[]) {
    this.count = decisionTrees.length;
    this.tableData = this.mapper.map(decisionTrees.length, decisionTrees);
  }

  @Input() title: string;

  count: number;
  tableData: TableData;

  private mapper: TableDataMapper<DecisionTree>;

  protected constructor(config: TableDataMapperConfiguration<DecisionTree>) {
    this.mapper = new TableDataMapper<DecisionTree>(config);
  }
}
