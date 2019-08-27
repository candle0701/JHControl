package com.jh.service.impl;

import com.jh.dao.MesProjectDetailMapper;
import com.jh.entity.MesProjectDetail;
import com.jh.service.MesProjectDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesProjectDetailServiceImpl implements MesProjectDetailService {

   @Autowired
   private  MesProjectDetailMapper  mesProjectDetailMapper;


   public int insertSelective(MesProjectDetail record) {
      return mesProjectDetailMapper.insertSelective(record);
   }
   public List<MesProjectDetail> findList(MesProjectDetail mesProjectDetail){
      return mesProjectDetailMapper.findList(mesProjectDetail);
   }

   public List<MesProjectDetail> find(MesProjectDetail mesProjectDetail){
      return mesProjectDetailMapper.find(mesProjectDetail);
   }

   public List<MesProjectDetail> findAll(MesProjectDetail mesProjectDetail){
      return mesProjectDetailMapper.findAll(mesProjectDetail);
   }
   public int update(MesProjectDetail mesProjectDetail){
      return mesProjectDetailMapper.update(mesProjectDetail);
   }

   public List<MesProjectDetail> pageList(MesProjectDetail mesProjectDetail,int page,int limit){
      return mesProjectDetailMapper.pageList(mesProjectDetail, page, limit);
   }

   @Override
   public MesProjectDetail query(MesProjectDetail mesProjectDetail) {
      return mesProjectDetailMapper.query(mesProjectDetail);
   }

   @Override
   public MesProjectDetail queryCount(MesProjectDetail mesProjectDetail) {
      return mesProjectDetailMapper.queryCount(mesProjectDetail);
   }

   @Override
   public MesProjectDetail getMesProjectDetail(String projectId,String winNo) {
      return mesProjectDetailMapper.getMesProjectDetail(projectId,winNo);
   }

   @Override
   public void updateALL(String projectId) {
      mesProjectDetailMapper.updateALL(projectId);
   }

   @Override
   public List<MesProjectDetail> selALL(MesProjectDetail projectDetails) {
      return mesProjectDetailMapper.selALL(projectDetails);
   }


   @Override
   public List<MesProjectDetail> allDateil(MesProjectDetail mesProjectDetail) {
      return mesProjectDetailMapper.allDateil(mesProjectDetail);
   }

   @Override
   public List<MesProjectDetail> all(MesProjectDetail mesProjectDetail) {
      return mesProjectDetailMapper.all(mesProjectDetail);
   }
}
