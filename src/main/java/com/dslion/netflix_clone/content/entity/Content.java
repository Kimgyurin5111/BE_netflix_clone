package com.dslion.netflix_clone.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

// content 테이블과 매핑되는 영화/시리즈 엔티티
@Entity
@Table(name = "content")
@Getter
@Setter
@NoArgsConstructor
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentType type;

    private Integer releaseYear;

    private String posterImageUrl;

    // 영화는 상영시간(분), 시리즈는 null
    private Integer runningTime;

    // 등록한 관리자(User)의 id만 저장 (연관관계 없이 단순하게)
    private Long createdBy;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Content(String title, String description, ContentType type,
                   Integer releaseYear, String posterImageUrl, Integer runningTime, Long createdBy) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.releaseYear = releaseYear;
        this.posterImageUrl = posterImageUrl;
        this.runningTime = runningTime;
        this.createdBy = createdBy;
    }
}
