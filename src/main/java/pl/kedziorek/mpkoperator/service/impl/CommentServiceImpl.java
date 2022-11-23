package pl.kedziorek.mpkoperator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.kedziorek.mpkoperator.domain.Comment;
import pl.kedziorek.mpkoperator.repository.CommentRepository;
import pl.kedziorek.mpkoperator.service.CommentService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public Comment saveComment(Comment comment) {
        comment.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        comment.setCreatedAt(LocalDateTime.now());
        log.info("Saving new comment to the database");
        return commentRepository.save(comment);
    }
}