package com.jh.dao;

import com.jh.entity.MesProjectDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesProjectDetailMapper {
    int insert(MesProjectDetail record);

    int insertSelective(MesProjectDetail record);

    List<MesProjectDetail> findList(MesProjectDetail mesProjectDetail);

    List<MesProjectDetail> find(MesProjectDetail mesProjectDetail);

    int update(MesProjectDetail mesProjectDetail);

    List<MesProjectDetail> findAll(MesProjectDetail mesProjectDetail);

    List<MesProjectDetail> pageList(@Param("mesProjectDetail")MesProjectDetail mesProjectDetail,@Param("page") int page,@Param("limit") int limit);
    List<MesProjectDetail> all(MesProjectDetail mesProjectDetail);

    MesProjectDetail query(MesProjectDetail mesProjectDetail);
    List<MesProjectDetail> allDateil(MesProjectDetail mesProjectDetail);
    MesProjectDetail queryCount(MesProjectDetail mesProjectDetail);
    MesProjectDetail getMesProjectDetail(@Param("projectId") String projectId,@Param("winNo")String winNo);

    List<MesProjectDetail> selALL(MesProjectDetail projectDetails);

    void updateALL(String projectId);

}