package org.tasks.myshop.dao.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.tasks.myshop.dao.model.ItemPicsEntity;

@Repository
public interface ItemPicsRepository extends ReactiveCrudRepository<ItemPicsEntity, Long> {
}
