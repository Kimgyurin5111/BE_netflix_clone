package com.dslion.netflix_clone.mylist.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

// my_list 테이블과 매핑되는 찜(마이리스트) 엔티티
// 연관관계 없이 userId, contentId를 그냥 값으로만 저장 (Content.createdBy와 같은 방식)
@Entity
@Table(name = "my_list", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "content_id"}))
@Getter
@Setter
@NoArgsConstructor
public class MyList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "content_id", nullable = false)
    private Long contentId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public MyList(Long userId, Long contentId) {
        this.userId = userId;
        this.contentId = contentId;
    }
}
