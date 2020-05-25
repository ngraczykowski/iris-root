package com.silenteight.serp.common.support.persistence;

import com.silenteight.serp.common.entity.IdentifiableEntity;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.persistence.EntityNotFoundException;

public class BasicInMemoryRepository<T extends IdentifiableEntity> {

  private final ConcurrentMap<Long, T> store = new ConcurrentHashMap<>();
  private final AtomicLong nextId = new AtomicLong(100 + new SecureRandom().nextInt(2000));

  @Nonnull
  public Iterable<T> saveAll(Iterable<T> entities) {
    entities.forEach(this::save);
    return entities;
  }

  @Nonnull
  public T save(T entity) {
    if (entity.getId() == null)
      entity.setId(nextId.getAndIncrement());

    store.put(entity.getId(), entity);

    return entity;
  }

  public void delete(T entity) {
    store.remove(entity.getId());
  }

  @Nonnull
  public T getById(long id) {
    return findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Entity " + id + " not found"));
  }

  public Optional<T> findById(long id) {
    return Optional.ofNullable(store.getOrDefault(id, null));
  }

  public int count() {
    return store.size();
  }

  protected final Map<Long, T> getInternalStore() {
    return store;
  }

  protected final Stream<T> stream() {
    return store.values().stream();
  }
}
