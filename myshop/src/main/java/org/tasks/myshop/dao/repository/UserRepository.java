package org.tasks.myshop.dao.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.tasks.myshop.dao.model.UserEntity;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity, Long> {

    Flux<UserEntity> findByUsername(String username);

}
