package com.fastcampus.boardserver.service.Impl;

import com.fastcampus.boardserver.dto.CategoryDTO;
import com.fastcampus.boardserver.mapper.CategoryMapper;
import com.fastcampus.boardserver.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public void register(String accountId, CategoryDTO categoryDTO) {
        if(accountId != null) {
            categoryMapper.register(categoryDTO);
        } else {
            log.error("error {}",categoryDTO);
            throw new RuntimeException("카테고리 등록 값을 확인해주세요." + categoryDTO);
        }
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        if (categoryDTO != null) {
            categoryMapper.updateCategory(categoryDTO);
        } else {
            log.error("error {}",categoryDTO);
            throw new RuntimeException("카테고리 수정 값을 확인해주세요." + categoryDTO);
        }
    }

    @Override
    public void delete(int categoryId) {
        if (categoryId != 0) {
            categoryMapper.deleteCategory(categoryId);
        } else {
            log.error("error {}",categoryId);
            throw new RuntimeException("카테고리 삭제 값을 확인해주세요." + categoryId);
        }
    }
}
