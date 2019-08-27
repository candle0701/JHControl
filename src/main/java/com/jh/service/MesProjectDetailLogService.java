package com.jh.service;

import com.jh.entity.MesProjectDetailLog;

import java.util.List;

public interface MesProjectDetailLogService {
    int insertSelective(MesProjectDetailLog mesProjectDetailLog);

    List<MesProjectDetailLog> list(MesProjectDetailLog mesProjectDetailLog);
}
