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

import java.util.ArrayList;
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
        List<ContentResponse> result = new ArrayList<>();

        for (MyList myList : myLists) {
            Content content = contentRepository.findById(myList.getContentId()).orElse(null);
            // 관리자가 이미 삭제한 콘텐츠는 목록에서 건너뛴다
            // (예전에는 예외를 던졌는데, 그러면 콘텐츠 하나만 지워져도 마이리스트 전체 조회가 실패했음)
            if (content != null) {
                result.add(ContentResponse.from(content));
            }
        }
        return result;
    }
}
