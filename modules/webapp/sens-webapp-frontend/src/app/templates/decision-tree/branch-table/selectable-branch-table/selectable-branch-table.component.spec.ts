import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BranchPageLoader } from '@app/templates/decision-tree/branch-table/branch-page-loader';
import { InMemoryBranchPageProvider } from '@app/templates/decision-tree/branch-table/in-memory-branch-page-provider';
import { SelectedBranchStore } from '@app/templates/decision-tree/branch-table/selectable-branch-table/selected-branch-store';
import { BranchModel } from '@app/templates/model/branch.model';
import { TestModule } from '@app/test/test.module';
import { BranchTableModule } from '../branch-table.module';
import { SelectableBranchTableComponent } from './selectable-branch-table.component';

describe('SelectableBranchTableComponent', () => {
  let component: SelectableBranchTableComponent;
  let fixture: ComponentFixture<SelectableBranchTableComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            BranchTableModule
          ]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectableBranchTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should store model when load success', () => {
    const model = <BranchModel> {featureNames: ['feature1']};
    const loader = new BranchPageLoader(new InMemoryBranchPageProvider([], model));
    const store = new SelectedBranchStore();
    component.loader = loader;
    component.store = store;

    loader.load(0, 10).subscribe();

    expect(store.getModel()).toEqual(model);
  });
});
