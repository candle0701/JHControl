package com.jh.service.impl;


import com.jh.dao.MesProcessQuotaMapper;
import com.jh.entity.MesProcessQuota;
import com.jh.service.MesProcessQuotaservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesProcessQuotaserviceImpl implements MesProcessQuotaservice {
        @Autowired
        private MesProcessQuotaMapper mesProcessQuotaMapper;

        @Override
        public List<MesProcessQuota> findCode(MesProcessQuota mesProcessQuota) {
                return mesProcessQuotaMapper.findCode(mesProcessQuota);
        }

        @Override
        public List<MesProcessQuota> findList(MesProcessQuota mesProcessQuota) {
                return mesProcessQuotaMapper.findList(mesProcessQuota);
        }

        @Override
        public int update(MesProcessQuota mesProcessQuota) {
                return mesProcessQuotaMapper.update(mesProcessQuota);
        }

        @Override
        public  List<MesProcessQuota> list(MesProcessQuota mesProcessQuota) {
                return mesProcessQuotaMapper.list(mesProcessQuota);
        }

        @Override
        public int insertSelective(MesProcessQuota mesProcessQuota) {
                return mesProcessQuotaMapper.insertSelective(mesProcessQuota);
        }

        @Override
        public MesProcessQuota find(MesProcessQuota mesProcessQuota) {
                return mesProcessQuotaMapper.find(mesProcessQuota);
        }
}
