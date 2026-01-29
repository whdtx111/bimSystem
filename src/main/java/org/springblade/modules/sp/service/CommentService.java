package org.springblade.modules.sp.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springblade.modules.sp.entity.Comment;
import org.springblade.modules.sp.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
@Slf4j
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    public Comment getCommentById(String id) {
        return commentMapper.getCommentById(id);
    }

    public List<Comment> getCommentsByIds(String[] id) {
        return commentMapper.getCommentsByIds(id);
    }

    public List<Comment> getCommentsByUrl(String url) {
        return commentMapper.getCommentsByUrl(url);
    }

    public List<Comment> getCommentsByCondition(String url) {
        return commentMapper.getCommentsByCondition(url);
    }

    public List<Comment> getAllComments() {
        return commentMapper.getAllComments();
    }

    public int insertComment(Comment comment) {
        return commentMapper.insertComment(comment);
    }

    public int updateComment(Comment comment) {
        return commentMapper.updateComment(comment);
    }

    public int deleteComment(String id) {
        return commentMapper.deleteComment(id);
    }

}
