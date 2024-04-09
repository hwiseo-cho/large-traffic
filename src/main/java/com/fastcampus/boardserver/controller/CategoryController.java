package com.fastcampus.boardserver.controller;

import com.fastcampus.boardserver.aop.LoginCheck;
import com.fastcampus.boardserver.dto.CategoryDTO;
import com.fastcampus.boardserver.service.CategoryService;
import com.fastcampus.boardserver.service.Impl.CategoryServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("categories")
@Log4j2
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = LoginCheck.UserType.ADMIN)
    public void register(@RequestParam(value = "accountId", required = false) String accountId, @RequestBody CategoryDTO categoryDTO) {
        categoryService.register(accountId, categoryDTO);
    }

    @PatchMapping("{categoryId}")
    @LoginCheck(type = LoginCheck.UserType.ADMIN)
    public void updateCategories(@RequestParam(value = "accountId", required = false) String accountId,
                                 @PathVariable(name = "categoryId") int categoryId,
                                 @RequestBody CategoryRequest categoryRequest) {
        CategoryDTO categoryDTO = new CategoryDTO(categoryId, categoryRequest.getName(), CategoryDTO.SortStatus.NEWEST,10,1);
        categoryService.update(categoryDTO);
    }

    @DeleteMapping("{categoryId}")
    @LoginCheck(type = LoginCheck.UserType.ADMIN)
    public void deleteCategories(@RequestParam(value = "accountId", required = false) String accountId,
                                 @PathVariable(name = "categoryId") int categoryId) {
        categoryService.delete(categoryId);
    }

    @Getter @Setter
    private static class CategoryRequest {
        private int id;
        private String name;
    }
}
