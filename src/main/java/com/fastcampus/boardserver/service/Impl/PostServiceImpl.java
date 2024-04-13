package com.fastcampus.boardserver.service.Impl;

import com.fastcampus.boardserver.dto.CommentDTO;
import com.fastcampus.boardserver.dto.PostDTO;
import com.fastcampus.boardserver.dto.TagDTO;
import com.fastcampus.boardserver.dto.UserDTO;
import com.fastcampus.boardserver.mapper.CommentMapper;
import com.fastcampus.boardserver.mapper.PostMapper;
import com.fastcampus.boardserver.mapper.TagMapper;
import com.fastcampus.boardserver.mapper.UserProfileMapper;
import com.fastcampus.boardserver.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final UserProfileMapper userProfileMapper;
    private final CommentMapper commentMapper;
    private final TagMapper tagMapper;

    @Override
    public void register(String id, PostDTO postDTO) {
        UserDTO memberInfo = userProfileMapper.getUserProfile(id);
        postDTO.setUserId(memberInfo.getId());
        postDTO.setCreateTime(new Date());

        if (memberInfo != null) {
            postMapper.register(postDTO);
            Integer postId = postDTO.getId();
            postDTO.getTagDTOList().forEach(t -> {
                tagMapper.register(t);
                Integer tagId = t.getId();
                tagMapper.createPostTag(tagId, postId);
            });
        } else {
            log.error("register ERROR! {}", postDTO);
            throw new RuntimeException("register ERROR! 상품 등록 메서드를 확인해주세요\n" + "Params : " + postDTO);
        }
    }

    @Override
    public List<PostDTO> getMyPosts(int accountId) {
        return postMapper.selectMyProducts(accountId);
    }

    @Override
    public void updatePosts(PostDTO postDTO) {
        if (postDTO != null && postDTO.getId() != 0 && postDTO.getUserId() != 0) {
            postMapper.updateProducts(postDTO);
        } else {
            log.error("updateProducts ERROR! {}", postDTO);
            throw new RuntimeException("updateProducts ERROR! 물품 변경 메서드를 확인해주세요\n" + "Params : " + postDTO);
        }
    }

    @Override
    public void deletePosts(int userId, int productId) {
        if (userId != 0 && productId != 0) {
            postMapper.deleteProduct(productId);
        } else {
            log.error("deleteProudct ERROR! {}", productId);
            throw new RuntimeException("updateProducts ERROR! 물품 삭제 메서드를 확인해주세요\n" + "Params : " + productId);
        }
    }

    @Override
    public void registerComment(CommentDTO commentDTO) {
        if(commentDTO.getPostId() != 0) {
            commentMapper.register(commentDTO);
        } else {
            log.error("registerComment {}", commentDTO);
            throw new RuntimeException("registerComment " + commentDTO);
        }
    }

    @Override
    public void updateComment(CommentDTO commentDTO) {
        if(commentDTO.getPostId() != 0) {
            commentMapper.updateComments(commentDTO);
        } else {
            log.error("updateComments {}", commentDTO);
            throw new RuntimeException("updateComments " + commentDTO);
        }
    }

    @Override
    public void deletePostComment(int userId, int commentId) {
        if(userId != 0 && commentId != 0) {
            commentMapper.deletePostComment(commentId);
        } else {
            log.error("deletePostComment {}", commentId);
            throw new RuntimeException("deletePostComment " + commentId);
        }
    }

    @Override
    public void registerTag(TagDTO tagDTO) {
        if(tagDTO != null) {
            tagMapper.register(tagDTO);
        } else {
            log.error("registerTag {}", tagDTO);
            throw new RuntimeException("registerTag " + tagDTO);
        }
    }

    @Override
    public void updateTag(TagDTO tagDTO) {
        if(tagDTO != null) {
            tagMapper.updateTags(tagDTO);
        } else {
            log.error("updateTags {}", tagDTO);
            throw new RuntimeException("updateTags " + tagDTO);
        }
    }

    @Override
    public void deletePostTag(int userId, int tagId) {
        if(userId != 0 && tagId != 0) {
            tagMapper.deletePostTag(tagId);
        } else {
            log.error("deletePostTag {}", tagId);
            throw new RuntimeException("deletePostTag " + tagId);
        }
    }

}
