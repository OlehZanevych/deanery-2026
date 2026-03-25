package org.lnu.teaching.web.application.design.deanery.repository.user;

import org.lnu.teaching.web.application.design.deanery.entity.projection.UserAuthDataProjection;
import org.lnu.teaching.web.application.design.deanery.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserAuthDataProjection findUserAuthDataByUsername(String username);

    @Modifying
    @Query(value = "DELETE FROM UserEntity WHERE id = :id")
    int removeById(Long id);
}
