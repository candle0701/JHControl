package com.jh.service;

import com.jh.entity.MesProjectDetail;

import java.util.List;

public interface MesProjectDetailService {


    int insertSelective(MesProjectDetail mesProjectDetail);

    List<MesProjectDetail> findList(MesProjectDetail mesProjectDetail);

    List<MesProjectDetail> find(MesProjectDetail mesProjectDetail);

    int update(MesProjectDetail mesProjectDetail);

    List<MesProjectDetail> findAll(MesProjectDetail mesProjectDetail);

    List<MesProjectDetail> pageList(MesProjectDetail mesProjectDetail, int page, int limit);

    List<MesProjectDetail> all(MesProjectDetail mesProjectDetail);

    MesProjectDetail query(MesProjectDetail mesProjectDetail);

    List<MesProjectDetail> allDateil(MesProjectDetail mesProjectDetail);

    MesProjectDetail queryCount(MesProjectDetail mesProjectDetail);

    MesProjectDetail getMesProjectDetail(String projectId,String winNo);

    List<MesProjectDetail> selALL(MesProjectDetail projectDetails);

    void updateALL(String projectId);
}
