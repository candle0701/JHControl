package com.jh.service;

import com.jh.entity.MesButton;

import java.util.List;

public interface MesButtonService {
    List<MesButton> getAllButton(String menuId,String roleId);
}
