package pl.kedziorek.mpkoperator.service;

import pl.kedziorek.mpkoperator.domain.Complaint;

import java.util.List;

public interface ComplaintService {
    Complaint saveComplaint(Complaint complaint);
    List<Complaint> getAllComplaints();
    List<Complaint> getComplaintsOfOneNotifier(String peselOfNotifier);
}
