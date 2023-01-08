package pl.kedziorek.mpkoperator.service;

import pl.kedziorek.mpkoperator.domain.Comment;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface CommentService {
    Comment saveComment(Comment comment);
    List<Comment> getAllCommentsOfComplaint(UUID uuid);
    List<Comment> getAllCommentsOfFault(UUID uuid);
}
