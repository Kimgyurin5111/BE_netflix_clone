package com.dslion.netflix_clone.mylist.repository;

import com.dslion.netflix_clone.mylist.entity.MyList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MyListRepository extends JpaRepository<MyList, Long> {

    boolean existsByUserIdAndContentId(Long userId, Long contentId);

    Optional<MyList> findByUserIdAndContentId(Long userId, Long contentId);

    List<MyList> findByUserId(Long userId);
}
