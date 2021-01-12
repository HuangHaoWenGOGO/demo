package seassoon.rule.converter;

import seassoon.rule.dto.AdminInputDTO;
import seassoon.rule.entity.Admin;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface AdminConverter {
    AdminInputDTO convertToAdminInputDTO(Admin admin);

    Admin convertToAdmin(AdminInputDTO adminInputDTO);
}
