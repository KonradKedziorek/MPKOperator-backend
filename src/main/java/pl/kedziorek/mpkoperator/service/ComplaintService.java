package pl.kedziorek.mpkoperator.service;

import pl.kedziorek.mpkoperator.domain.Complaint;
import pl.kedziorek.mpkoperator.domain.dto.ComplaintRequest;

import java.util.List;
import java.util.UUID;

public interface ComplaintService {
    Complaint saveComplaint(ComplaintRequest complaint);
    List<Complaint> getAllComplaints();
    Complaint findByUuid(UUID uuid);
    Complaint updateComplaint(Complaint complaint, UUID uuid);
}
