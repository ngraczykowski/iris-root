import { SelectableItemDiscriminator } from './selectable-dynamic-table.component';
import { SelectableItemStore } from './selectable-item-store';

export class SelectableTablePageService<T> {

  private visibleItems: T[];

  constructor(private discriminator: SelectableItemDiscriminator<T>, private store: SelectableItemStore<T>) { }

  setVisibleItems(visibleItems: T[]) {
    this.visibleItems = visibleItems;
  }

  areAllSelected(): boolean {
    for (let index = 0; index < this.visibleItems.length; index++) {
      if (!this.store.get(this.getId(index))) {
        return false;
      }
    }
    return this.visibleItems.length > 0;
  }

  isSelected(index: number): boolean {
    return !!this.store.get(this.getId(index));
  }

  toggleAll() {
    const next = !this.areAllSelected();
    for (let index = 0; index < this.visibleItems.length; index++) {
      this.selectItem(index, next);
    }
  }

  toggleOne(index: number) {
    const next = !this.isSelected(index);
    this.selectItem(index, next);
  }

  private getId(index: number) {
    return this.discriminator.getId(this.visibleItems[index]);
  }

  private selectItem(index: number, next: boolean) {
    const id = this.getId(index);
    if (next) {
      this.store.add(id, this.visibleItems[index]);
    } else {
      this.store.remove(id);
    }
  }
}
