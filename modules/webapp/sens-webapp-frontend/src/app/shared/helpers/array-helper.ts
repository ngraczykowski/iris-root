export default class ArrayHelper {
  static unique(array) {
    return Array.from(new Set(array));
  }

  static compareArrays(array: Object[], otherArray: Object[],
    objectCompareCallback?: Function): boolean {
    if (array === otherArray) {
      return true;
    }
    if ((!array && otherArray) || (array && !otherArray)) {
      return false;
    }
    if (array && otherArray) {
      return (array.length === otherArray.length) && this.compareItemsInArrays(array, otherArray, objectCompareCallback);
    }
    return false;
  }

  private static compareItemsInArrays(array: Object[], otherArray: Object[], objectCompareCallback?: Function): boolean {
    let allObjectsSame = true;
    if (!objectCompareCallback) {
      objectCompareCallback = this.defaultObjectComparator;
    }
    array.forEach((value, index) => {
      allObjectsSame = allObjectsSame && objectCompareCallback.call(value, value, otherArray[index]);
    });
    return allObjectsSame;
  }

  private static defaultObjectComparator(v1: any, v2: any): boolean {
    return v1 === v2;
  }
}
