package com.dslion.netflix_clone.content.entity;

import com.dslion.netflix_clone.genre.entity.Genre;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    // 콘텐츠 하나에 장르 여러 개, 장르 하나도 콘텐츠 여러 개에 속할 수 있음 (다대다)
    // open-in-view: false 설정이라 화면(응답)에서 지연 로딩 예외가 나지 않도록 즉시 로딩(EAGER) 사용
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "content_genre",
            joinColumns = @JoinColumn(name = "content_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

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
