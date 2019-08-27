package com.jh.service;

import com.jh.entity.MesProcessQuota;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface MesProcessQuotaservice {

        List<MesProcessQuota> findList(MesProcessQuota mesProcessQuota);

        int update(MesProcessQuota mesProcessQuota);

        List<MesProcessQuota> list(MesProcessQuota mesProcessQuota);

        int insertSelective(MesProcessQuota record);
        MesProcessQuota find(MesProcessQuota mesProcessQuota);

        List<MesProcessQuota> findCode(MesProcessQuota mesProcessQuota);


}
