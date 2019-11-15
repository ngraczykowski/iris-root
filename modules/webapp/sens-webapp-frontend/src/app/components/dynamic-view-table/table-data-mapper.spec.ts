import { DynamicComponent, View } from '../dynamic-view/dynamic-view.component';
import { CellViewFactory, LabelViewFactory, TableDataMapper } from './table-data-mapper';

class EntryMock {
  field1: string;
  field2: number;
}

class DynamicComponentMock implements DynamicComponent {
  data;
}

class ViewMock extends View {
  constructor(data) {
    super(DynamicComponentMock, data);
  }
}

class LabelViewFactoryMock implements LabelViewFactory {
  constructor(private label) {}

  create(): View {
    return new ViewMock(this.label);
  }
}

class CellViewFactoryMock1 implements CellViewFactory<EntryMock> {
  create(entry: EntryMock): View {
    return new ViewMock(entry.field1);
  }
}

class CellViewFactoryMock2 implements CellViewFactory<EntryMock> {
  create(entry: EntryMock): View {
    return new ViewMock(entry.field2);
  }
}

describe('TableDataMapper', () => {
  let mapper: TableDataMapper<EntryMock>;

  beforeEach(() => {
    mapper = new TableDataMapper<EntryMock>({
      columnDefinitions: [
        {
          labelFactory: new LabelViewFactoryMock('column1'),
          cellFactory: new CellViewFactoryMock1()
        },
        {
          labelFactory: new LabelViewFactoryMock('column2'),
          cellFactory: new CellViewFactoryMock2()
        }
      ]
    });
  });

  it('should map response correctly', () => {
    const tableData = mapper.map(10, [
      {field1: 'value1', field2: 1},
      {field1: 'value2', field2: 2}
    ]);

    expect(tableData.total).toEqual(10);
    expect(tableData.labels).toEqual([
      {view: new ViewMock('column1')},
      {view: new ViewMock('column2')}
    ]);
    expect(tableData.rows).toEqual([
      {views: [new ViewMock('value1'), new ViewMock(1)]},
      {views: [new ViewMock('value2'), new ViewMock(2)]}
    ]);
  });
});
