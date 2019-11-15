import {
  Label,
  Row,
  TableData
} from '@app/components/dynamic-view-table/dynamic-view-table.component';
import { View } from '@app/components/dynamic-view/dynamic-view.component';
import {
  TablePage,
  TablePageMapper
} from '@app/components/pageable-dynamic-table/simple-table-data-provider';
import { SelectableTablePageService } from './selectable-table-page-service';
import {
  SelectAllViewComponent,
  SelectAllViewData
} from './views/select-all-view/select-all-view.component';
import {
  SelectOneViewComponent,
  SelectOneViewData
} from './views/select-one-view/select-one-view.component';

export class SelectableTablePageMapper<T> implements TablePageMapper<T> {

  constructor(private innerMapper: TablePageMapper<T>, private selectionService: SelectableTablePageService<T>) { }

  map(page: TablePage<T>): TableData {
    return this.mapToTableDataWithSelection(this.innerMapper.map(page));
  }

  private mapToTableDataWithSelection(original: TableData) {
    return <TableData> {
      total: original.total,
      labels: this.getLabels(original),
      rows: this.getRows(original)
    };
  }

  private getRows(tableData: TableData) {
    return tableData.rows.map((r, index) => {
      const views = r.views.slice();
      views.unshift(this.createSelectOneView(index));
      return <Row> {views: views};
    });
  }

  private getLabels(tableData: TableData) {
    const labels = tableData.labels.slice();
    labels.unshift(<Label> {view: this.createSelectAllView()});
    return labels;
  }

  private createSelectOneView(index): View {
    return <View> {
      component: SelectOneViewComponent,
      data: <SelectOneViewData> {
        service: this.selectionService,
        index: index
      }
    };
  }

  private createSelectAllView(): View {
    return <View> {
      component: SelectAllViewComponent,
      data: <SelectAllViewData> {
        service: this.selectionService
      }
    };
  }
}
