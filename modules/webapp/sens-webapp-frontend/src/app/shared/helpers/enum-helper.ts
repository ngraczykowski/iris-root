export default class EnumHelper {
  static indexOf(stringEnumObject, enumValue) {
    const roles: String[] = Object.keys(stringEnumObject);
    const index = roles.findIndex(r => r === enumValue.valueOf());
    if (index >= 0) {
      return index;
    } else {
      throw new Error(`Could not find index of ${enumValue}`);
    }
  }
}
