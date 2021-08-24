public interface StorageManager {

  void create(String storageName);

  void remove(String storageName);

  boolean storageExist(String storageName);
}
