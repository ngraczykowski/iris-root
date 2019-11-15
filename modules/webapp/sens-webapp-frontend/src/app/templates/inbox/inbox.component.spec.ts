import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { EMPTY } from 'rxjs';
import { TestModule } from '../../test/test.module';

import { InboxComponent, } from './inbox.component';
import { InboxModule } from './inbox.module';
import { SolvedMessageTableDataProvider } from './solved-message-table-data-provider';
import { UnsolvedMessageTableDataProvider } from './unsolved-message-table-data-provider';

describe('InboxComponent', () => {
  let component: InboxComponent;
  let fixture: ComponentFixture<InboxComponent>;

  let unsolvedMessageTableDataProvider: UnsolvedMessageTableDataProvider;
  let solvedMessageTableDataProvider: SolvedMessageTableDataProvider;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            InboxModule
          ]
        })
        .compileComponents();

    unsolvedMessageTableDataProvider = TestBed.get(UnsolvedMessageTableDataProvider);
    solvedMessageTableDataProvider = TestBed.get(SolvedMessageTableDataProvider);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InboxComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    spyOn(unsolvedMessageTableDataProvider, 'getPage').and.returnValue(EMPTY);
    spyOn(solvedMessageTableDataProvider, 'getPage').and.returnValue(EMPTY);

    fixture.detectChanges();

    expect(component).toBeTruthy();
  });
});
