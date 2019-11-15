import {
  CellViewFactory,
  LabelViewFactory
} from '../../../../components/dynamic-view-table/table-data-mapper';
import { View } from '../../../../components/dynamic-view/dynamic-view.component';
import { DateFormatter } from '../../../../shared/date/date-formatter';
import { InboxMessage } from '../../../model/inbox.model';
import { ActionViewComponent, ActionViewData } from './action-view/action-view.component';
import { DisabledBranchDescriptionViewComponent } from './disabled-branch-description-view/disabled-branch-description-view.component';
import { SimpleViewComponent, SimpleViewData } from './simple-view/simple-view.component';

export interface SimpleLabelConfiguration {
  className: string;
  text: string;
}

export class SimpleLabelViewFactory implements LabelViewFactory {

  constructor(private configuration: SimpleLabelConfiguration) {}

  create(): View {
    return <View> {
      component: SimpleViewComponent,
      data: <SimpleViewData> {
        className: this.configuration.className,
        text: this.configuration.text
      }
    };
  }
}

export class CreationDateCellViewFactory implements CellViewFactory<InboxMessage> {
  create(inboxMessage: InboxMessage): View {
    return <View> {
      component: SimpleViewComponent,
      data: <SimpleViewData> {
        className: 'inbox-message-cell-creation-date',
        text: DateFormatter.format(inboxMessage.date)
      }
    };
  }
}

export class ActionCellViewFactory implements CellViewFactory<InboxMessage> {
  create(inboxMessage: InboxMessage): View {
    return <View> {
      component: ActionViewComponent,
      data: <ActionViewData> {
        inboxMessageId: inboxMessage.id
      }
    };
  }
}

export class DefaultDescriptionCellViewFactory implements CellViewFactory<InboxMessage> {
  create(inboxMessage: InboxMessage): View {
    return <View> {
      component: SimpleViewComponent,
      data: <SimpleViewData> {
        className: 'inbox-message-cell-default-description',
        text: inboxMessage.message
      }
    };
  }
}

export class DisabledBranchDescriptionViewFactory implements CellViewFactory<InboxMessage> {
  create(inboxMessage: InboxMessage): View {
    return <View> {
      component: DisabledBranchDescriptionViewComponent,
      data: inboxMessage
    };
  }
}

export interface DescriptionTypeDefinition {
  type: string;
  cellFactory: CellViewFactory<InboxMessage>;
}

export class DescriptionCellViewFactoryConfiguration {
  typeDefinitions: DescriptionTypeDefinition[];
  defaultCellFactory: CellViewFactory<InboxMessage>;
}

export class DescriptionCellViewFactory implements CellViewFactory<InboxMessage> {

  private readonly defaultFactory;
  private readonly factoryMap: Map<string, CellViewFactory<InboxMessage>>;

  constructor(private configuration: DescriptionCellViewFactoryConfiguration) {
    this.factoryMap = this.buildFactoryMap();
    this.defaultFactory = this.configuration.defaultCellFactory;
  }

  create(inboxMessage: InboxMessage): View {
    const factoryFunction = this.factoryMap.get(inboxMessage.type);
    if (factoryFunction) {
      return factoryFunction.create(inboxMessage);
    }
    return this.defaultFactory.create(inboxMessage);
  }

  private buildFactoryMap(): Map<string, CellViewFactory<InboxMessage>> {
    const factoryMap = new Map<string, CellViewFactory<InboxMessage>>();
    this.configuration.typeDefinitions
        .forEach(d => factoryMap.set(d.type, d.cellFactory));
    return factoryMap;
  }
}
