package ru.neirodev.mehanik.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.neirodev.mehanik.dto.ServiceAnnouncementDTO;
import ru.neirodev.mehanik.entity.ServiceAnnouncementEntity;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ServiceAnnouncementMapper {

    ServiceAnnouncementMapper INSTANCE = Mappers.getMapper(ServiceAnnouncementMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateServiceAnnouncementFromDTO(ServiceAnnouncementDTO serviceAnnouncementDTO,
                                          @MappingTarget ServiceAnnouncementEntity serviceAnnouncement);
}
