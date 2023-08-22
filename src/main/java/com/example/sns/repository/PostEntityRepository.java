package com.example.sns.repository;

import com.example.sns.model.entity.PostEntity;
import com.example.sns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostEntityRepository extends JpaRepository<PostEntity, Integer> {

    // 아래 JPQL과 우측 쿼리 메서드는 같음 //Page<PostEntity> findAllByUser(UserEntity entity, Pageable pageable);
    @Query("select p from PostEntity p where p.user = :entity")
    Page<PostEntity> findAllByUser(@Param("entity") UserEntity entity, Pageable pageable);

}
