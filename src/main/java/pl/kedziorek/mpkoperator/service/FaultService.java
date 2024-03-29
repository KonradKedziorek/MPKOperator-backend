package pl.kedziorek.mpkoperator.service;

import pl.kedziorek.mpkoperator.domain.Comment;
import pl.kedziorek.mpkoperator.domain.Fault;
import pl.kedziorek.mpkoperator.domain.dto.request.FaultRequest;
import pl.kedziorek.mpkoperator.domain.dto.response.DataResponse;
import pl.kedziorek.mpkoperator.domain.enums.FaultStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface FaultService<T> {
    Fault saveFault(FaultRequest faultRequest);
    Fault findByUuid(UUID uuid);
    Fault updateFaultStatus(FaultStatus faultStatus, UUID uuid);
    DataResponse<T> getFaults(Map<String, String> params, int page, int size);
    Fault saveOrUpdateFault(FaultRequest faultRequest);
    List<Comment> createComment(UUID uuid, String content);
}
