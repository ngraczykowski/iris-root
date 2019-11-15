import { TableData } from '@app/components/dynamic-view-table/dynamic-view-table.component';
import {
  CellViewFactory,
  LabelViewFactory,
  TableDataMapperConfiguration
} from '@app/components/dynamic-view-table/table-data-mapper';
import { DynamicComponent, View } from '@app/components/dynamic-view/dynamic-view.component';
import { DecisionTree } from '@model/decision-tree.model';
import { DecisionTreeTableBaseComponent } from './decision-tree-table-base.component';

class TestViewComponent implements DynamicComponent {
  data: string;
}

class TestEntryLabelViewFactory implements LabelViewFactory {
  create(): View {
    return {
      component: TestViewComponent,
      data: 'label'
    };
  }
}

class TestEntryCellViewFactory implements CellViewFactory<DecisionTree> {
  create(entry: DecisionTree): View {
    return {
      component: TestViewComponent,
      data: entry.name
    };
  }
}

class TestComponent extends DecisionTreeTableBaseComponent {

  constructor() {
    super(<TableDataMapperConfiguration<DecisionTree>>{
      columnDefinitions: [
        {
          labelFactory: new TestEntryLabelViewFactory(),
          cellFactory: new TestEntryCellViewFactory()
        }
      ]
    });
  }
}

describe('DecisionTreeTableBaseComponent', () => {

  let component: DecisionTreeTableBaseComponent;

  beforeEach(() => {
    component = new TestComponent();
  });

  it('should change count on set decision trees collection', () => {
    component.decisionTrees = [<DecisionTree> {name: 'name1'}];
    expect(component.count).toEqual(1);

    component.decisionTrees = [<DecisionTree> {name: 'name1'}, <DecisionTree> {name: 'name2'}];
    expect(component.count).toEqual(2);
  });

  it('should change table data on set decision trees collection', () => {
    component.decisionTrees = [<DecisionTree> {name: 'name1'}];
    expect(component.tableData).toEqual(<TableData> {
      total: 1,
      labels: [{view: {component: TestViewComponent, data: 'label'}}],
      rows: [
        {views: [{component: TestViewComponent, data: 'name1'}]}
      ]
    });

    component.decisionTrees = [<DecisionTree> {name: 'name1'}, <DecisionTree> {name: 'name2'}];
    expect(component.tableData).toEqual(<TableData> {
      total: 2,
      labels: [{view: {component: TestViewComponent, data: 'label'}}],
      rows: [
        {views: [{component: TestViewComponent, data: 'name1'}]},
        {views: [{component: TestViewComponent, data: 'name2'}]}
      ]
    });
  });
});
