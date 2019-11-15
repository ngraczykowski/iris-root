import { BatchType, BatchTypeListBuilder } from './batch-type-list-builder';

describe('BatchTypeListBuilder', () => {

  function assertBatchTypes(actual: BatchType[], expected: BatchType[]) {
    expect(actual).toEqual(expected);
  }

  it('should create empty batch types when pass null assignments', () => {
    const batchTypes = new BatchTypeListBuilder(null, []).build();

    assertBatchTypes(batchTypes, []);
  });

  it('should create empty batch types when pass undefined assignments', () => {
    const batchTypes = new BatchTypeListBuilder(undefined, []).build();

    assertBatchTypes(batchTypes, []);
  });

  it('should create empty batch types when pass null activations', () => {
    const batchTypes = new BatchTypeListBuilder([], null).build();

    assertBatchTypes(batchTypes, []);
  });

  it('should create empty batch types when pass undefined activations', () => {
    const batchTypes = new BatchTypeListBuilder([], undefined).build();

    assertBatchTypes(batchTypes, []);
  });

  it('should create inactive batchTypes when pass some assignments', () => {
    const batchTypes = new BatchTypeListBuilder(['a', 'b', 'c'], []).build();

    assertBatchTypes(batchTypes, [
      {value: 'a', active: false},
      {value: 'b', active: false},
      {value: 'c', active: false}
    ]);
  });

  it('should create only active batchTypes when pass equal and not empty activations and assignments', () => {
    const batchTypes = new BatchTypeListBuilder(['a', 'b', 'c'], ['a', 'b', 'c']).build();

    assertBatchTypes(batchTypes, [
      {value: 'a', active: true},
      {value: 'b', active: true},
      {value: 'c', active: true}
    ]);
  });

  it('should create active and inactive batchTypes in valid order', () => {
    const batchTypes = new BatchTypeListBuilder(['a', 'b', 'c', 'd'], ['c', 'd']).build();

    assertBatchTypes(batchTypes, [
      {value: 'c', active: true},
      {value: 'd', active: true},
      {value: 'a', active: false},
      {value: 'b', active: false}
    ]);
  });
});
