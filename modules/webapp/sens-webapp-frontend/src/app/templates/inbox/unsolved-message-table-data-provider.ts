import { Injectable } from '@angular/core';
import {
  TableDataMapper,
  TableDataMapperConfiguration
} from '../../components/dynamic-view-table/table-data-mapper';
import { InboxMessage } from '../model/inbox.model';
import { InboxMessageTableDataProvider } from './inbox-table-data-mapper';
import { InboxService } from './inbox.service';
import {
  ActionCellViewFactory,
  CreationDateCellViewFactory,
  DefaultDescriptionCellViewFactory,
  DescriptionCellViewFactory,
  DisabledBranchDescriptionViewFactory,
  SimpleLabelViewFactory
} from './message-table/views/view-factories';

const mapperConfig: TableDataMapperConfiguration<InboxMessage> = {
  columnDefinitions: [
    {
      labelFactory: new SimpleLabelViewFactory({
        className: 'inbox-message-label-description',
        text: 'inbox.messagesList.columns.description'
      }),
      cellFactory: new DescriptionCellViewFactory({
        typeDefinitions: [
          {
            type: 'REASONING_BRANCH_DISABLED',
            cellFactory: new DisabledBranchDescriptionViewFactory()
          }
        ],
        defaultCellFactory: new DefaultDescriptionCellViewFactory()
      })
    },
    {
      labelFactory: new SimpleLabelViewFactory({
        className: 'inbox-message-label-creation-date',
        text: 'inbox.messagesList.columns.creationDate'
      }),
      cellFactory: new CreationDateCellViewFactory(),
    },
    {
      labelFactory: new SimpleLabelViewFactory({
        className: 'inbox-message-label-action',
        text: 'inbox.messagesList.columns.action'
      }),
      cellFactory: new ActionCellViewFactory()
    }
  ]
};

@Injectable()
export class UnsolvedMessageTableDataProvider extends InboxMessageTableDataProvider {

  constructor(inboxService: InboxService) {
    super((page, size) => inboxService.getUnsolvedMessages(page, size),
        new TableDataMapper(mapperConfig));
  }
}
