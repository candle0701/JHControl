package com.jh.dao;

import com.jh.entity.MesMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesMenuMapper {
    List<MesMenu> findAllMenuByRoleId(@Param("role_id")String role_id);
    List<MesMenu> findPermissionMenuByRoleId(@Param("role_id")String role_id);

    List<MesMenu> findMenuParentIdIsNotNull(@Param("mrmRoleId")String mrmRoleId);

    List<MesMenu> getAllMenu();
    List<MesMenu> getMenuNameWx(@Param("roleId")String roleId,@Param("usersId")String usersId);

    MesMenu getParentMenu(@Param("id")String id);
}
