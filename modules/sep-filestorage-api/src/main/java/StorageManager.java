public interface StorageManager {

  void createBucket(String bucketName);

  void removeBucket(String bucketName);

  boolean bucketExist(String bucketName);
}
