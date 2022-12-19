package pl.kedziorek.mpkoperator.service;

import pl.kedziorek.mpkoperator.domain.Comment;

import java.util.Set;
import java.util.UUID;

public interface CommentService {
    Comment saveComment(Comment comment);
    Set<Comment> getAllCommentsOfComplaint(UUID uuid);
    Set<Comment> getAllCommentsOfFault(UUID uuid);
}
