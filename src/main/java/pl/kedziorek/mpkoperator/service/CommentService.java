package pl.kedziorek.mpkoperator.service;

import pl.kedziorek.mpkoperator.domain.Comment;

public interface CommentService {
    Comment saveComment(Comment comment);
}
