package v1.amachon.domain.base;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class BaseEntity {

    public enum Status {
        NORMAL, EXPIRED;
    }

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @PrePersist
    public void init() {
        this.status = Status.NORMAL;
    }

    public void expired() {
        this.status = Status.EXPIRED;
    }
}
