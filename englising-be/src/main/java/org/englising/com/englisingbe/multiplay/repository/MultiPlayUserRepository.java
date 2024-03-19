package org.englising.com.englisingbe.multiplay.repository;

import org.englising.com.englisingbe.multiplay.entity.MultiPlayUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MultiPlayUserRepository extends JpaRepository<Long, MultiPlayUser> {
    MultiPlayUser findByMultiplayId(Long multiPlayId);
    MultiPlayUser save(MultiPlayUser multiPlayUser);
}
