package com.dslion.netflix_clone.mylist.service;

import com.dslion.netflix_clone.content.dto.response.ContentResponse;
import com.dslion.netflix_clone.content.entity.Content;
import com.dslion.netflix_clone.content.repository.ContentRepository;
import com.dslion.netflix_clone.global.exception.ContentNotFoundException;
import com.dslion.netflix_clone.global.exception.DuplicateMyListException;
import com.dslion.netflix_clone.global.exception.MyListNotFoundException;
import com.dslion.netflix_clone.mylist.entity.MyList;
import com.dslion.netflix_clone.mylist.repository.MyListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// 찜(마이리스트) 등록/삭제/조회의 실제 처리 로직
@Service
public class MyListService {

    @Autowired
    private MyListRepository myListRepository;

    @Autowired
    private ContentRepository contentRepository;

    // 마이리스트에 추가 (이미 있는 콘텐츠거나, 존재하지 않는 콘텐츠면 예외)
    public void add(Long userId, Long contentId) {
        if (!contentRepository.existsById(contentId)) {
            throw new ContentNotFoundException();
        }
        if (myListRepository.existsByUserIdAndContentId(userId, contentId)) {
            throw new DuplicateMyListException();
        }

        MyList myList = new MyList(userId, contentId);
        myListRepository.save(myList);
    }

    // 마이리스트에서 삭제
    public void remove(Long userId, Long contentId) {
        MyList myList = myListRepository.findByUserIdAndContentId(userId, contentId)
                .orElseThrow(MyListNotFoundException::new);
        myListRepository.delete(myList);
    }

    // 내 마이리스트 조회 (찜해둔 콘텐츠 목록)
    public List<ContentResponse> findMyList(Long userId) {
        List<MyList> myLists = myListRepository.findByUserId(userId);

        return myLists.stream()
                .map(myList -> {
                    Content content = contentRepository.findById(myList.getContentId())
                            .orElseThrow(ContentNotFoundException::new);
                    return ContentResponse.from(content);
                })
                .toList();
    }
}
