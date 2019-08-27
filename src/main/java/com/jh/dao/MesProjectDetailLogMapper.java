package com.jh.dao;

import com.jh.entity.MesProjectDetailLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesProjectDetailLogMapper {
    int insert(MesProjectDetailLog record);

    int insertSelective(MesProjectDetailLog record);

    List<MesProjectDetailLog> list(MesProjectDetailLog mesProjectDetailLog);



}