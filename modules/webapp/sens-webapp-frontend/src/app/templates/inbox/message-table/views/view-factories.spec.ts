import { CellViewFactory } from '../../../../components/dynamic-view-table/table-data-mapper';
import { DynamicComponent, View } from '../../../../components/dynamic-view/dynamic-view.component';
import { InboxMessage } from '../../../model/inbox.model';
import { DescriptionCellViewFactory } from './view-factories';

describe('ViewFactories', () => {
  describe('DescriptionCellViewFactory', () => {
    let factory: DescriptionCellViewFactory;

    class DynamicComponentMock implements DynamicComponent {
      data;
    }

    class ViewMock extends View {
      constructor(data) {
        super(DynamicComponentMock, data);
      }
    }

    class CellViewFactoryMock1 implements CellViewFactory<InboxMessage> {
      create(entry: InboxMessage): View {
        return new ViewMock(entry.extra.data1);
      }
    }

    class CellViewFactoryMock2 implements CellViewFactory<InboxMessage> {
      create(entry: InboxMessage): View {
        return new ViewMock(entry.extra.data2);
      }
    }

    class CellViewFactoryMock3 implements CellViewFactory<InboxMessage> {
      create(entry: InboxMessage): View {
        return new ViewMock(entry.extra.data3);
      }
    }

    beforeEach(() => {
      factory = new DescriptionCellViewFactory({
        typeDefinitions: [
          {
            type: 'type1',
            cellFactory: new CellViewFactoryMock1()
          },
          {
            type: 'type2',
            cellFactory: new CellViewFactoryMock2()
          }
        ],
        defaultCellFactory: new CellViewFactoryMock3()
      });
    });

    it('should create view correctly when type1 message', () => {
      const view = factory.create(<InboxMessage>{
        type: 'type1',
        extra: {
          'data1': 'value1'
        }
      });

      expect(view).toEqual(new ViewMock('value1'));
    });

    it('should create view correctly when pass type2 message', () => {
      const view = factory.create(<InboxMessage>{
        type: 'type2',
        extra: {
          'data2': 'value2'
        }
      });

      expect(view).toEqual(new ViewMock('value2'));
    });

    it('should create view correctly when pass unknown message', () => {
      const view = factory.create(<InboxMessage>{
        type: 'unknown',
        extra: {
          'data3': 'value3'
        }
      });

      expect(view).toEqual(new ViewMock('value3'));
    });
  });
});

