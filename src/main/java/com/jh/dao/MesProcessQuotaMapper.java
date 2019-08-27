package com.jh.dao;

import com.jh.entity.MesProcessQuota;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesProcessQuotaMapper {
    int insert(MesProcessQuota mesProcessQuota);

    int insertSelective(MesProcessQuota mesProcessQuota);


    List<MesProcessQuota> findList(MesProcessQuota mesProcessQuota);

    int update(MesProcessQuota mesProcessQuota);

    List<MesProcessQuota> list(MesProcessQuota mesProcessQuota);

    MesProcessQuota find(MesProcessQuota mesProcessQuota);

    List<MesProcessQuota> findCode(MesProcessQuota mesProcessQuota);
}