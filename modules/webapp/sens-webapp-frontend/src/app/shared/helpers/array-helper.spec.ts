import ArrayHelper from './array-helper';

describe('ArrayHelper', () => {

  it('empty array should return empty', () => {
    expect(ArrayHelper.unique([])).toEqual([]);
  });

  it('array with duplicates should return unique array', () => {
    expect(ArrayHelper.unique([1, 2, 1])).toEqual([1, 2]);
    expect(ArrayHelper.unique(['1', '2', '1'])).toEqual(['1', '2']);
    expect(ArrayHelper.unique([1, 9, 2, 8, 3, 7, 4, 6, 5, 1, 9, 2, 8, 3, 7, 4, 6, 5]))
        .toEqual([1, 9, 2, 8, 3, 7, 4, 6, 5]);
  });

  it('array without duplicates should return same array', () => {
    expect(ArrayHelper.unique([1, 2])).toEqual([1, 2]);
  });

  it('can compare simple arrays', () => {
    expect(ArrayHelper.compareArrays(undefined, undefined)).toBeTruthy();
    expect(ArrayHelper.compareArrays([], [])).toBeTruthy();
    expect(ArrayHelper.compareArrays([1], [1])).toBeTruthy();

    expect(ArrayHelper.compareArrays(undefined, [])).toBeFalsy();
    expect(ArrayHelper.compareArrays([], undefined)).toBeFalsy();
    expect(ArrayHelper.compareArrays([1], [])).toBeFalsy();
    expect(ArrayHelper.compareArrays([], [1])).toBeFalsy();
    expect(ArrayHelper.compareArrays([1], ['1'])).toBeFalsy();
  });

  it('can compare array of objects', () => {
    const obj1 = {
      'prop1': 'value1'
    };
    const obj2 = {
      'prop1': 'value1'
    };
    const obj3 = {
      'prop1': 'value2'
    };
    const obj4 = {
      'prop1': 'value1',
      'prop2': 'value2'
    };

    const objComparator = (v1, v2) => v1['prop1'] === v2['prop1'];

    expect(ArrayHelper.compareArrays([obj1], [obj1])).toBeTruthy();

    expect(ArrayHelper.compareArrays([obj1], [obj2])).toBeFalsy();

    expect(ArrayHelper.compareArrays([obj1], [])).toBeFalsy();

    expect(ArrayHelper.compareArrays([obj1], [obj2], objComparator)).toBeTruthy();

    expect(ArrayHelper.compareArrays([obj1], [obj3], objComparator)).toBeFalsy();

    expect(ArrayHelper.compareArrays([obj1], [], objComparator)).toBeFalsy();

    expect(ArrayHelper.compareArrays([obj1], [obj4], objComparator)).toBeTruthy();

    expect(ArrayHelper.compareArrays([obj1, obj1], [obj2, obj4], objComparator)).toBeTruthy();

    expect(ArrayHelper.compareArrays([obj1, obj2], [obj4, obj1], objComparator)).toBeTruthy();

    expect(ArrayHelper.compareArrays([obj1, obj3], [obj2, obj3], objComparator)).toBeTruthy();

    expect(ArrayHelper.compareArrays([obj1, obj3], [obj3, obj1], objComparator)).toBeFalsy();

    expect(ArrayHelper.compareArrays([obj1, obj3], [obj1], objComparator)).toBeFalsy();

    expect(ArrayHelper.compareArrays([obj1], [obj1, obj3], objComparator)).toBeFalsy();
  });
});
