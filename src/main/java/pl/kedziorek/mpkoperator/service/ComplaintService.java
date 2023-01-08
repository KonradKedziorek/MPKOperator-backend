package pl.kedziorek.mpkoperator.service;

import pl.kedziorek.mpkoperator.domain.Comment;
import pl.kedziorek.mpkoperator.domain.Complaint;
import pl.kedziorek.mpkoperator.domain.dto.request.ComplaintRequest;
import pl.kedziorek.mpkoperator.domain.dto.response.DataResponse;
import pl.kedziorek.mpkoperator.domain.enums.ComplaintStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ComplaintService<T> {
    Complaint saveOfUpdateComplaint(ComplaintRequest complaint);
    Complaint findByUuid(UUID uuid);
    Complaint updateComplaintStatus(ComplaintStatus complaint, UUID uuid);
    DataResponse<T> getComplaints(Map<String, String> params, int page, int size);
    List<Comment> createComment(UUID uuid, String content);
}
