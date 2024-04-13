package com.fastcampus.boardserver.controller;

import com.fastcampus.boardserver.aop.LoginCheck;
import com.fastcampus.boardserver.dto.CommentDTO;
import com.fastcampus.boardserver.dto.PostDTO;
import com.fastcampus.boardserver.dto.TagDTO;
import com.fastcampus.boardserver.dto.UserDTO;
import com.fastcampus.boardserver.dto.request.PostSearchRequest;
import com.fastcampus.boardserver.dto.response.CommonResponse;
import com.fastcampus.boardserver.service.Impl.PostSearchServiceImpl;
import com.fastcampus.boardserver.service.Impl.PostServiceImpl;
import com.fastcampus.boardserver.service.Impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/posts")
@Log4j2
@RequiredArgsConstructor
public class PostController {

    private final UserServiceImpl userService;
    private final PostServiceImpl postService;
    private final PostSearchServiceImpl postSearchService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = {LoginCheck.UserType.USER, LoginCheck.UserType.ADMIN})
    public ResponseEntity<CommonResponse<PostDTO>> registerPost(@RequestParam(value = "accountId", required = false) String accountId,
                                                                @RequestBody PostDTO postDTO
                                                                ) {
        postService.register(accountId, postDTO);
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "SUCCESS", "registerPost", postDTO);
        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping("my-posts")
    @LoginCheck(type = {LoginCheck.UserType.USER, LoginCheck.UserType.ADMIN})
    public ResponseEntity<CommonResponse<PostDTO>> myPostInfo(@RequestParam(value = "accountId", required = false) String accountId) {
        UserDTO userDTO = userService.getUserInfo(accountId);
        List<PostDTO> list = postService.getMyPosts(userDTO.getId());
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "SUCCESS", "myPostInfo", list);
        return ResponseEntity.ok(commonResponse);
    }

    @PatchMapping("{postId}")
    @LoginCheck(type = {LoginCheck.UserType.USER, LoginCheck.UserType.ADMIN})
    public ResponseEntity<CommonResponse<PostRequest>> updatePosts(@RequestParam(value = "accountId", required = false) String accountId,
                                                                   @PathVariable(name = "postId") int postId,
                                                                   @RequestBody PostRequest postRequest) {
        UserDTO memberInfo = userService.getUserInfo(accountId);
        PostDTO postDTO = PostDTO.builder()
                .id(postId)
                .name(postRequest.getName())
                .contents(postRequest.getContents())
                .views(postRequest.getViews())
                .categoryId(postRequest.getCategoryId())
                .userId(memberInfo.getId())
                .fileId(postRequest.getFileId())
                .updateTime(new Date())
                .build();
        postService.updatePosts(postDTO);
        CommonResponse commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "updatePosts", postDTO);
        return ResponseEntity.ok(commonResponse);
    }

    @DeleteMapping("{postId}")
    @LoginCheck(type = {LoginCheck.UserType.USER, LoginCheck.UserType.ADMIN})
    public ResponseEntity<CommonResponse<PostDeleteRequest>> deleteposts(@RequestParam(value = "accountId", required = false) String accountId,
                                                                         @PathVariable(name = "postId") int postId,
                                                                         @RequestBody PostDeleteRequest postDeleteRequest) {
        UserDTO memberInfo = userService.getUserInfo(accountId);
        postService.deletePosts(memberInfo.getId(), postId);
        CommonResponse commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "deleteposts", postDeleteRequest);
        return ResponseEntity.ok(commonResponse);
    }

    // comment
    @PostMapping("comments")
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<CommentDTO>> registerPostComment(String accountId, @RequestBody CommentDTO commentDTO) {
        postService.registerComment(commentDTO);
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "SUCCESS", "registerPostComment", commentDTO);

        return ResponseEntity.ok(commonResponse);
    }

    @PatchMapping("comments/{commentId}")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<CommentDTO>> updatePostComment(String accountId,
                                                                        @PathVariable(name="commentId") int commentId,
                                                                        @RequestBody CommentDTO commentDTO) {
        UserDTO userDTO = userService.getUserInfo(accountId);

        if(userDTO != null) {
            postService.updateComment(commentDTO);
        }
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "SUCCESS", "updatePostComment", commentDTO);
        return ResponseEntity.ok(commonResponse);
    }

    @DeleteMapping("comments/{commentId}")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<CommentDTO>> deletePostComment(String accountId,
                                                                        @PathVariable(name="commentId") int commentId) {
        UserDTO userDTO = userService.getUserInfo(accountId);

        if(userDTO != null) {
            postService.deletePostComment(userDTO.getId(), commentId);
        }
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "SUCCESS", "deletePostComment", commentId);
        return ResponseEntity.ok(commonResponse);
    }

    // tag
    @PostMapping("tags")
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<TagDTO>> registerPostTag(String accountId, @RequestBody TagDTO tagDTO) {
        postService.registerTag(tagDTO);
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "SUCCESS", "registerPostTag", tagDTO);
        return ResponseEntity.ok(commonResponse);
    }

    @PatchMapping("tags/{tagId}")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<CommentDTO>> updatePostTag(String accountId,
                                                                        @PathVariable(name="tagId") int tagId,
                                                                        @RequestBody TagDTO tagDTO) {
        UserDTO userDTO = userService.getUserInfo(accountId);

        if(userDTO != null) {
            postService.updateTag(tagDTO);
        }
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "SUCCESS", "updatePostTag", tagDTO);
        return ResponseEntity.ok(commonResponse);
    }

    @DeleteMapping("tags/{tagId}")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<CommentDTO>> deletePostTag(String accountId,
                                                                        @PathVariable(name="tagId") int tagId) {
        UserDTO userDTO = userService.getUserInfo(accountId);

        if(userDTO != null) {
            postService.deletePostTag(userDTO.getId(), tagId);
        }
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "SUCCESS", "deletePostTag", tagId);
        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping
    public PostResponse searchByTagName(String tagName) {
        List<PostDTO> postDTOList = postSearchService.getPostByTag(tagName);
        return new PostResponse(postDTOList);
    }


    @Getter
    @AllArgsConstructor
    public static class PostResponse {
        private List<PostDTO> postDTOs;
    }

    @Getter @Setter
    private static class PostRequest {
        private String name;
        private String contents;
        private int views;
        private int categoryId;
        private int userId;
        private int fileId;
        private Date updateTime;
    }

    @Setter
    @Getter
    private static class PostDeleteRequest {
        private int id;
        private int accountId;
    }
}
