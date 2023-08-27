package com.example.sns.repository;

import com.example.sns.model.entity.AlarmEntity;
import com.example.sns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface AlarmEntityRepository extends JpaRepository<AlarmEntity, Integer> {

    @Query("select a from AlarmEntity a where a.user = :entity")
    Page<AlarmEntity> findAllByUser(@Param("entity") UserEntity user, Pageable pageable);

}
