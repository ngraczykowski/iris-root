import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { View } from '../dynamic-view/dynamic-view.component';

export interface TableData {
  total: number;
  labels: Label[];
  rows: Row[];
}

export interface Label {
  view: View;
}

export interface Row {
  views: View[];
}

@Component({
  selector: 'app-dynamic-view-table',
  templateUrl: './dynamic-view-table.component.html',
  styleUrls: ['./dynamic-view-table.component.scss']
})
export class DynamicViewTableComponent implements OnInit {

  readonly perfectScrollbarConfig = {
    suppressScrollY: true,
    wheelPropagation: true,
  };

  elementPreviewData = [];
  elementPreviewStatus = false;

  @Input() translatePrefix;
  @Input() inProgress: boolean;
  @Input() error: any;
  @Input() tableData: TableData;
  @Input() tableName: string;
  @Input() previewElements: boolean;

  @Output() previewElement: EventEmitter<any> = new EventEmitter();

  constructor() { }

  private getTableRowId(id) {
    return this.tableName + '-' + id;
  }

  trackByFn(index, view) {
    return view.id;
  }

  ngOnInit() {
  }

  elementPreview(rowData) {
    this.elementPreviewData = [];
    this.makePreviewValues(this.tableData.labels, rowData);
    this.elementPreviewStatus = true;
  }

  makePreviewValues(labels, values) {
    labels.forEach((key, index) => {
      const keyName = key.view.data.text;

      switch (keyName) {
        case undefined:
          break;
        case 'Branch ID':
          this.pushValueToTemporary(keyName, `${values.views[index].data.decisionTreeId}-${values.views[index].data.matchGroupId}`);
          break;
        case 'Solution':
          this.pushValueToTemporary(keyName, values.views[index].data.decision);
          break;
        case 'Status':
          this.pushValueToTemporary(keyName, this.booleanToValue(values.views[index].data.enabled, 'Enabled', 'Disabled'));
          break;
        case 'Status of changes':
          this.pushValueToTemporary(keyName, this.booleanToValue(values.views[index].data.enabled, 'Pending', 'No Changes'));
          break;
        case 'Score':
          this.pushValueToTemporary(keyName, values.views[index].data.score);
          break;
        case 'Review status':
          this.pushValueToTemporary(keyName, this.booleanToValue(values.views[index].data.reviewed, 'Reviewed', 'Waiting for review'));
          break;
        default:
          this.pushValueToTemporary(keyName, values.views[index].data.value);
      }
    });
  }

  pushValueToTemporary(key, value) {
    this.elementPreviewData.push({
      'name': key,
      'value': value
    });
  }

  booleanToValue(value, trueValue, falseValue) {
    if (value === true) {
      return trueValue;
    } else {
      return falseValue;
    }
  }
}
