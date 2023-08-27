package com.example.sns.model.entity;

import com.example.sns.model.AlarmArgs;
import com.example.sns.model.AlarmType;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "\"alarm\"", indexes = {
        @Index(name = "user_id_idx", columnList = "user_id")
})
@Getter
@Setter
@SQLDelete(sql = "UPDATE \"alarm\" SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
@TypeDef(name = "json", typeClass = JsonType.class)
public class AlarmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 알람을 받은 사람
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    // json 형태로 저장되어 있는 문자열 데이터를 Map으로 매핑합니다.
    // 추후 args 값 가져올 때 AlarmArgs 클래스로 변환하는 로직 만들어야 문제 없을 것
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private Map<String, Object> args;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    void registeredAt(){
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt(){
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static AlarmEntity of(UserEntity userEntity, AlarmType alarmType, AlarmArgs args){
        AlarmEntity entity = new AlarmEntity();
        entity.setUser(userEntity);
        entity.setAlarmType(alarmType);
        Map<String, Object> argsMap = Map.of(
                "fromUserId", args.getFromUserId(),
                "targetId", args.getTargetId()
        );
        entity.setArgs(argsMap);
        return entity;
    }

    public AlarmArgs getArgs() {
        AlarmArgs args = new AlarmArgs();
        args.setTargetId((Integer) this.args.get("targetId"));
        args.setFromUserId((Integer) this.args.get("fromUserId"));
        return args;
    }
}
