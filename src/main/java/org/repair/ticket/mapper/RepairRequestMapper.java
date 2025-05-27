package org.repair.ticket.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.model.ResponseDetailsDto;
import org.openapitools.model.TicketResponseDto;
import org.repair.ticket.model.RepairRequest;

@Mapper(componentModel = "spring", uses = CommentMapper.class)
public interface RepairRequestMapper {

    @Mapping(target = "status", expression = "java(mapStatus(entity.getStatus()))")
    @Mapping(target = "createdBy", source = "createdBy.username")
    @Mapping(target = "assignedTo", source = "assignedTo.username")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "comments", source = "comments")
    ResponseDetailsDto toDetailsDto(RepairRequest entity);

    @Mapping(target = "status", expression = "java(mapStatus(entity.getStatus()))")
    @Mapping(target = "assignedTo", source = "assignedTo.username")
    @Mapping(target = "createdAt", source = "createdAt")
    TicketResponseDto toDto(RepairRequest entity);


    default org.openapitools.model.Status mapStatus(org.repair.ticket.dictionary.Status status) {
        return status == null ? null : org.openapitools.model.Status.valueOf(status.name());
    }
}