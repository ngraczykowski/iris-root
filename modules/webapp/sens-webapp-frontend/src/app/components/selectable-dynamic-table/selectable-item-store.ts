export class SelectableItemStore<T> {

  private storeMap: Map<any, T> = new Map<any, T>();

  clear() {
    this.storeMap.clear();
  }

  add(id: any, item: T) {
    this.storeMap.set(id, item);
  }

  get(id: any): T {
    return this.storeMap.get(id);
  }

  remove(id: any) {
    this.storeMap.delete(id);
  }

  getAll(): T[] {
    return Array.from(this.storeMap.values());
  }

  length(): Number {
    return this.storeMap.size;
  }

}
