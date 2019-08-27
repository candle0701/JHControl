package com.jh.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jh.entity.*;
import com.jh.service.*;
import com.jh.utils.CurrentTime;
import com.jh.utils.UUIDUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("mesProject")
public class MesProjectController {


    @Autowired
    private MesProjectService mesProjectService;
    @Autowired
    private MesProjectDetailService  mesProjectDetailService;
    @Autowired
    private MesProjectDetailLogService mesProjectDetailLogService;

    @Autowired
    private MesMenuButtonService mesMenuButtonService;

    @Autowired
    private MesMessageService mesMessageService;

    @Autowired
    private MesUsersService mesUsersService;

    @RequestMapping("toMesProjectList")
    public  String toMesProjectList(Model model,MesProject mesProject,String menuId,HttpSession session){
        MesUsers mesUsers = (MesUsers) session.getAttribute("mesUsers");
        if(mesUsers == null){
            return "redirect:/";
        }else {
            List<MesMenuButton> menuButtonList = mesMenuButtonService.getButtonByRoleIdAndMenuId(mesUsers.getMesRoleUsers().getRoleId(), menuId);
            model.addAttribute("menuButtonList", menuButtonList);
            return "mes_project/mes_project_list";
        }
    }


    @RequestMapping("mesProjectFrom")
    public  String mesProjectFrom(Model model,MesProject mesProject){
        if(StringUtils.isNotEmpty(mesProject.getProjectId())){
            mesProject=mesProjectService.selectByPrimaryKey(mesProject);
            model.addAttribute("mesProject",mesProject);
            return "mes_project/mes_project_update";
        }
        return "mes_project/mes_project_From";
    }

    @ResponseBody
    @RequestMapping("list")
    public  JSONObject list( MesProject mesProject,int page,int limit){
        List<MesProject> list=  mesProjectService.findList(mesProject,(page-1)*limit,page*limit);
        for (MesProject project : list) {
            MesUsers mesUsers=mesUsersService.findUser(project.getMan());
            project.setMan(mesUsers.getNickname());
            if(StringUtils.isNotEmpty(project.getChecker())){
                MesUsers user=mesUsersService.findUser(project.getChecker());
                project.setChecker(user.getNickname());
            }

        }
        List<MesProject> listCount=  mesProjectService.findAllProject(mesProject);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("msg","");
        jsonObject.put("code",0);
        jsonObject.put("count",listCount.size());
        JSONArray array= JSONArray.parseArray(JSON.toJSONString(list));
        jsonObject.put("data",array);
      return jsonObject;
    }

    @ResponseBody
    @RequestMapping("add")
    public DataInfo add(String projectName,String bDate,String eDate,String projectDetail, HttpSession session){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMM-dd");
        DataInfo dataInfo=new DataInfo();
        List<MesProjectDetail> mpd = JSONObject.parseArray(projectDetail, MesProjectDetail.class);
        for (int i=0; i<mpd.size(); i++){
            for (int j=0; j<mpd.size() ;j++){
                if(!mpd.get(i).getId().equals(mpd.get(j).getId())){
                    if(mpd.get(i).getWinNo().equals(mpd.get(j).getWinNo())){
                        dataInfo.setStatus(false);
                        dataInfo.setMsg("窗号出现重复窗号出现重复。");
                        return dataInfo;
                    }
                }
            }
        }

        MesProject mesProject=new MesProject();
        mesProject.setProjectName(projectName);
        mesProject.setCreateDate(df.format(new Date()));
        List<MesProject> list=mesProjectService.find(mesProject);
        dataInfo.setStatus(true);
       if(list.size() == 0){
           SimpleDateFormat data = new SimpleDateFormat("yyyyMMdd");
           String year=data.format(new Date());
           mesProject.setProjectId("XM"+year+String.valueOf( mesProjectService.selAll().size()+1));
           mesProject.setBdate(bDate);
           mesProject.setEdate(eDate);
           MesUsers mesUsers=(MesUsers)session.getAttribute("mesUsers");
           mesProject.setMan(mesUsers.getUsername());
           mesProject.setState("0");
           int num=mesProjectService.insertSelective(mesProject);
           if(num > 0){

               MesProjectDetail mesProjectDetail=new MesProjectDetail();
               JSONArray jsonArray=JSONArray.parseArray(projectDetail);
               System.out.println(jsonArray);
               if(jsonArray.size()>0) {
                   for (int i = 0; i < jsonArray.size(); i++) {
                       JSONObject jsonObject = jsonArray.getJSONObject(i);
                       mesProjectDetail.setId(UUID.randomUUID().toString().replace("-", ""));
                       mesProjectDetail.setTempId(mesProjectDetail.getId());
                       mesProjectDetail.setWinNo(jsonObject.getString("winNo"));//窗号
                       mesProjectDetail.setWinTypeId(jsonObject.getString("winTypeId")); //窗型
                       mesProjectDetail.setNum(jsonObject.getString("num"));//数量
                       mesProjectDetail.setBlueprintWidth(jsonObject.getString("blueprintWidth"));//图纸宽
                       mesProjectDetail.setDrawingHeight(jsonObject.getString("drawingHeight"));//图纸高
                       mesProjectDetail.setWinWidth(jsonObject.getString("winWidth"));//洞口宽
                       mesProjectDetail.setWinHeight(jsonObject.getString("winHeight"));//洞口高
                       mesProjectDetail.setPreTotal(jsonObject.getString("preTotal"));//暂估工程量，总量
                       mesProjectDetail.setPrice(jsonObject.getString("price"));//合同单价
                       mesProjectDetail.setCreateDate(df.format(new Date()));
                       mesProjectDetail.setVer("0");
                       mesProjectDetail.setItem("0");
                       mesProjectDetail.setProjectId(mesProject.getProjectId());
                       mesProjectDetailService.insertSelective(mesProjectDetail);

                       MesProjectDetailLog  mesProjectDetailLog=new MesProjectDetailLog();
                       mesProjectDetailLog.setId(UUID.randomUUID().toString().replace("-", ""));
                       mesProjectDetailLog.setWinNo(jsonObject.getString("winNo"));//窗号
                       mesProjectDetailLog.setWinTypeId(jsonObject.getString("winTypeId")); //窗型
                       mesProjectDetailLog.setNum(jsonObject.getString("num"));//数量
                       mesProjectDetailLog.setBlueprintWidth(jsonObject.getString("blueprintWidth"));//图纸宽
                       mesProjectDetailLog.setDrawingHeight(jsonObject.getString("drawingHeight"));//图纸高
                       mesProjectDetailLog.setWinWidth(jsonObject.getString("winWidth"));//洞口宽
                       mesProjectDetailLog.setWinHeight(jsonObject.getString("winHeight"));//洞口高
                       mesProjectDetailLog.setPreTotal(jsonObject.getString("preTotal"));//暂估工程量，总量
                       mesProjectDetailLog.setPrice(jsonObject.getString("price"));//合同单价
                       mesProjectDetailLog.setCreateDate(df.format(new Date()));
                       mesProjectDetailLog.setStatus("1");
                       mesProjectDetailLog.setProjectId(mesProjectDetail.getProjectId());
                       mesProjectDetailLog.setCreateBy(mesUsers.getUsername());
                       mesProjectDetailLogService.insertSelective(mesProjectDetailLog);

                   }
               }

           }else{
               dataInfo.setStatus(false);
               dataInfo.setMsg("项目抬头添加失败，请重试。");
           }
       }else{
           dataInfo.setStatus(false);
           dataInfo.setMsg("项目名称已存在。");
       }
        return dataInfo;
    }






    @ResponseBody
    @RequestMapping("update")
    public  DataInfo update( MesProject mesProject, HttpSession session){
        DataInfo dataInfo=new DataInfo();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MesUsers mesUsers=(MesUsers)session.getAttribute("mesUsers");
        try {
            mesProject=mesProjectService.sel(mesProject);
            if(mesProject.getState().equals("1")){
                dataInfo.setMsg("删除失败，项目已审核通过");
                dataInfo.setStatus(false);
                return dataInfo;
            }
            mesProject.setDel("1");
            int count=mesProjectService.updateByPrimaryKeySelective(mesProject);
            if(count>0){
                MesProjectDetail mesProjectDetail=new  MesProjectDetail();
                mesProjectDetail.setProjectId(mesProject.getProjectId());
                List<MesProjectDetail> list=mesProjectDetailService.findList(mesProjectDetail);
                for (MesProjectDetail projectDetail: list){



                    MesProjectDetailLog  mesProjectDetailLog=new MesProjectDetailLog();
                    mesProjectDetailLog.setId(UUID.randomUUID().toString().replace("-", ""));
                    mesProjectDetailLog.setWinNo(projectDetail.getWinNo());//窗号
                    mesProjectDetailLog.setWinTypeId(projectDetail.getWinTypeId()); //窗型
                    mesProjectDetailLog.setNum(projectDetail.getNum());//数量
                    mesProjectDetailLog.setBlueprintWidth(projectDetail.getBlueprintWidth());//图纸宽
                    mesProjectDetailLog.setDrawingHeight(projectDetail.getDrawingHeight());//图纸高
                    mesProjectDetailLog.setWinWidth(projectDetail.getWinWidth());//洞口宽
                    mesProjectDetailLog.setWinHeight(projectDetail.getWinHeight());//洞口高
                    mesProjectDetailLog.setPreTotal(projectDetail.getPreTotal());//暂估工程量，总量
                    mesProjectDetailLog.setPrice(projectDetail.getPrice());//合同单价
                    mesProjectDetailLog.setCreateDate(df.format(new Date()));
                    mesProjectDetailLog.setStatus("2");
                    mesProjectDetailLog.setProjectId(projectDetail.getId());
                    mesProjectDetailLog.setCreateBy(mesUsers.getUsername());
                    mesProjectDetailLogService.insertSelective(mesProjectDetailLog);



                }
                dataInfo.setMsg("删除成功");
                dataInfo.setStatus(true);
                return dataInfo;
            }else{
                dataInfo.setMsg("删除失败");
                dataInfo.setStatus(false);
                return dataInfo;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            dataInfo.setMsg("删除失败");
            dataInfo.setStatus(false);
            return dataInfo;
        }



    }

    @ResponseBody
    @RequestMapping("audit")
    public  DataInfo audit( MesProject mesProject,HttpSession session){
        DataInfo dataInfo=new DataInfo();
        MesProject project=mesProjectService.selectByPrimaryKey(mesProject);
        if(project.getState().equals("1")){
            dataInfo.setMsg("请勿重复审核");
            return dataInfo;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MesUsers mesUsers=(MesUsers)session.getAttribute("mesUsers");
        MesUsers user= mesUsersService.findUser(project.getMan());
        mesProject.setChecker(mesUsers.getUsername());
        mesProject.setCheckDate(simpleDateFormat.format(new Date()));
        if(mesProject.getState().equals("1")){
            mesProject.setProjectRemark("");
        }
        int count=mesProjectService.updateByPrimaryKeySelective(mesProject);
        if(mesProject.getState().equals("1") ){
            MesMessage mesMessage = new MesMessage();
            mesMessage.setId(UUIDUtil.getUUID());
            mesMessage.setIsRead("0");
            mesMessage.setCreateDate(simpleDateFormat.format(new Date()));
            mesMessage.setSender(mesUsers.getNickname());
            mesMessage.setSenderId(user.getId());
            mesMessage.setMessageType("项目通过审核");
            mesMessage.setReciverId(user.getId());
            mesMessage.setContent("您提交的项目"+project.getProjectId()+"通过审核！");
            mesMessage.setReciver("d27d15a74a5b4ca89d9a7707e423a7f6");
            mesMessageService.insertSelective(mesMessage);
            if(mesProject.getState().equals("1")){
                dataInfo.setMsg("审核成功");
            }

        }else{
            MesMessage mesMessage = new MesMessage();
            mesMessage.setId(UUIDUtil.getUUID());
            mesMessage.setIsRead("0");
            mesMessage.setCreateDate(simpleDateFormat.format(new Date()));
            mesMessage.setSender(mesUsers.getNickname());
            mesMessage.setSenderId(mesUsers.getId());
            mesMessage.setMessageType("项目审核驳回");
            mesMessage.setReciverId(user.getId());
            mesMessage.setReciver("d27d15a74a5b4ca89d9a7707e423a7f6");
            mesMessage.setContent("您提交的项目"+project.getProjectId()+"被驳回,请注意检查！");
            mesMessageService.insertSelective(mesMessage);
            if(mesProject.getState().equals("2")){
                dataInfo.setMsg("驳回审核成功");
            }

        }
        return dataInfo;
    }


    @ResponseBody
    @RequestMapping("find")
    public  DataInfo find( MesProject mesProject){
        DataInfo dataInfo=new DataInfo();
        List<MesProject> list=mesProjectService.find(mesProject);
        if(list.size()>0){
            dataInfo.setMsg("项目名称已存在.");
            dataInfo.setStatus(true);
        }
        return dataInfo;
    }

    @ResponseBody
    @RequestMapping("mesProjectFind")
    public  DataInfo mesProjectFind( MesProject mesProject){
        DataInfo dataInfo=new DataInfo();
        List<MesProject> list=mesProjectService.findUp(mesProject);
        if(list.size()>0){
            dataInfo.setMsg("项目名称已存在.");
            dataInfo.setStatus(true);
        }
        return dataInfo;
    }

    @ResponseBody
    @RequestMapping("projectFind")
    public  DataInfo projectFind( MesProject mesProject){
        DataInfo dataInfo=new DataInfo();
        List<MesProject> list=mesProjectService.find(mesProject);
        if(list.size()>0){
            dataInfo.setMsg("项目名称已存在.");
            dataInfo.setStatus(true);
        }
        return dataInfo;
    }
    @RequestMapping("mesProjectAudit")
    public  String mesProjectAudit(Model model,MesProject mesProject){
        model.addAttribute("mesProject",mesProject);
        return "mes_project/mes_project_audit";
    }



    @ResponseBody
    @RequestMapping("save")
    public DataInfo save(String projectName,String bdate,String edate, HttpSession session){
        DataInfo dataInfo=new DataInfo();
        MesProject mesProject=new MesProject();
        SimpleDateFormat data = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String year=data.format(new Date());
        mesProject.setProjectId("XM"+year+String.valueOf( mesProjectService.selAll().size()+1));
        mesProject.setProjectName(projectName);
        mesProject.setBdate(bdate);
        mesProject.setEdate(edate);
        MesUsers mesUsers=(MesUsers)session.getAttribute("mesUsers");
        mesProject.setMan(mesUsers.getUsername());
        mesProject.setState("0");
        mesProject.setCreateDate(simpleDateFormat.format(new Date()));
        int num=mesProjectService.insertSelective(mesProject);
        if(num>0){
            dataInfo.setStatus(true);
            dataInfo.setMsg("添加成功");
        }else{
            dataInfo.setStatus(false);
            dataInfo.setMsg("添加失败");
        }
        return dataInfo;
    }


    @ResponseBody
    @RequestMapping("updateSave")
    public DataInfo updateSave(String projectId,String projectName,String bdate,String edate, HttpSession session){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MesUsers mesUsers=(MesUsers)session.getAttribute("mesUsers");
        DataInfo dataInfo=new DataInfo();
        MesProject mesProject=new MesProject();
        mesProject.setProjectName(projectName);
        mesProject.setProjectId(projectId);
        mesProject.setBdate(bdate);
        mesProject.setEdate(edate);
        int num=mesProjectService.updateByPrimaryKeySelective(mesProject);
        if(num>0){
            dataInfo.setStatus(true);
            dataInfo.setMsg("修改成功");
        }else{
            dataInfo.setStatus(false);
            dataInfo.setMsg("修改失败");
        }
        return dataInfo;
    }




    /**
     * 模板导出
     * @param session
     * @param response
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("createExcel")
    public  String createExcel(HttpSession session,HttpServletResponse response) throws IOException{
        // 新建一个Excel的工作空间
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet=workbook.createSheet("项目模板");
        HSSFRow row=sheet.createRow(0);
        row.createCell(0).setCellValue("项目名称");
        row.createCell(1).setCellValue("窗号");
        row.createCell(2).setCellValue("甲方窗号");
        row.createCell(3).setCellValue("窗型");
        row.createCell(4).setCellValue("数量(数值类型)");
        row.createCell(5).setCellValue("图纸宽(数值类型)");
        row.createCell(6).setCellValue("图纸高(数值类型)");
        row.createCell(7).setCellValue("洞口宽(数值类型)");
        row.createCell(8).setCellValue("洞口高(数值类型)");
        row.createCell(9).setCellValue("暂估工程量(数值类型)");
        row.createCell(10).setCellValue("合同单价(数值类型)");

        OutputStream output= null;
        try {
            String name="项目模板";
            output = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment;filename="+new String(name.getBytes("gbk"), "iso8859-1")+".xls");
            response.setContentType("application/msexcel");
            workbook.write(output);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    @ResponseBody
    @RequestMapping("importExcel")
    @Transactional(noRollbackFor = Exception.class)
    public  JSONObject importExcel(@RequestParam(value = "file")MultipartFile file,HttpServletRequest request, HttpServletResponse response,HttpSession session){
        int  number=0;;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MesUsers mesUsers=(MesUsers)session.getAttribute("mesUsers");
        String fileName=file.getOriginalFilename();
        try{
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            int rowLength = sheet.getLastRowNum()+1;//总行数
            int colLength = sheet.getRow(0).getLastCellNum();   //总列数
            MesProject project=new MesProject();
            List<MesProjectDetail> list=new ArrayList<MesProjectDetail>();
            for (int i = 1; i < rowLength; i++) {
                MesProjectDetail mesProjectDetail=new MesProjectDetail();
                number=i+1;
                Row row = sheet.getRow(i);


                String projectName = null;//项目名称
                try {
                    row.getCell(0).setCellType(CellType.STRING);
                    projectName = row.getCell(0).getStringCellValue();
                } catch (Exception e) {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("code",0);
                    jsonObject.put("msg","第"+number+"行,项目名称类型不正确,请检查数据。");
                    return jsonObject;
                }

                MesProject mesProject=new MesProject();
                mesProject.setProjectName(projectName);
                 List<MesProject>  mesProjectList=mesProjectService.find(mesProject);
                if(mesProjectList.size() == 0 ){
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("code",0);
                    jsonObject.put("msg","第"+number+"行项目名称不存在,请检查数据。");
                    return jsonObject;
                }else{
                    if(i==1){
                        MesProjectDetail projectDetail=new MesProjectDetail();
                        projectDetail.setProjectId(mesProjectList.get(0).getProjectId());
                        List<MesProjectDetail> list1=mesProjectDetailService.findList(projectDetail);
                        if(list1.size()>0){
                            JSONObject jsonObject=new JSONObject();
                            jsonObject.put("code",0);
                            jsonObject.put("msg","该项目已存在数据，请先删除数据.");
                            return jsonObject;
                        }
                    }

                }




                mesProjectDetail.setProjectId(mesProjectList.get(0).getProjectId());

                String winNo = null;//窗号
                try {
                    row.getCell(1).setCellType(CellType.STRING);
                    winNo = row.getCell(1).getStringCellValue();
                    mesProjectDetail.setWinNo(winNo);
                } catch (Exception e) {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("code",0);
                    jsonObject.put("msg","第"+number+"行,窗号类型不正确,请检查数据。");
                    return jsonObject;
                }



                try {
                    row.getCell(2).setCellType(CellType.STRING);
                    String partyWinNo = row.getCell(2).getStringCellValue();//甲方窗号
                    mesProjectDetail.setPartyWinNo(partyWinNo);
                } catch (Exception e) {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("code",0);
                    jsonObject.put("msg","第"+number+"行,甲方窗号类型不正确,请检查数据。");
                    return jsonObject;
                }

                try {
                    row.getCell(3).setCellType(CellType.STRING);
                    String winTypeId = row.getCell(3).getStringCellValue();//窗型
                    mesProjectDetail.setWinTypeId(winTypeId);
                } catch (Exception e) {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("code",0);
                    jsonObject.put("msg","第"+number+"行,窗型类型不正确,请检查数据。");
                    return jsonObject;
                }

                try {
                    row.getCell(4).setCellType(CellType.NUMERIC);
                    double num = row.getCell(4).getNumericCellValue();//数量
                    mesProjectDetail.setNum(String.valueOf((int)num));
                } catch (Exception e) {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("code",0);
                    jsonObject.put("msg","第"+number+"行,数量类型不正确,请检查数据。");
                    return jsonObject;
                }

                try {
                    row.getCell(5).setCellType(CellType.NUMERIC);
                    double  blueprintWidth =row.getCell(5).getNumericCellValue();//图纸宽
                    mesProjectDetail.setBlueprintWidth(String.valueOf((int)blueprintWidth));
                } catch (Exception e) {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("code",0);
                    jsonObject.put("msg","第"+number+"行,图纸宽类型不正确,请检查数据。");
                    return jsonObject;
                }

                try {
                    row.getCell(6).setCellType(CellType.NUMERIC);
                    double  drawingHeight =row.getCell(6).getNumericCellValue();;//图纸高
                    mesProjectDetail.setDrawingHeight(String.valueOf((int)drawingHeight));
                } catch (Exception e) {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("code",0);
                    jsonObject.put("msg","第"+number+"行,图纸高类型不正确,请检查数据。");
                    return jsonObject;
                }

                try {
                    row.getCell(7).setCellType(CellType.NUMERIC);
                    double  winWidth =  row.getCell(7).getNumericCellValue();//洞口宽
                    mesProjectDetail.setWinWidth(String.valueOf((int)winWidth));
                } catch (Exception e) {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("code",0);
                    jsonObject.put("msg","第"+number+"行,洞口宽类型不正确,请检查数据。");
                    return jsonObject;
                }

                try {
                    row.getCell(8).setCellType(CellType.NUMERIC);
                    double  winHeight = row.getCell(8).getNumericCellValue();//洞口高
                    mesProjectDetail.setWinHeight(String.valueOf((int)winHeight));
                } catch (Exception e) {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("code",0);
                    jsonObject.put("msg","第"+number+"行,洞口高类型不正确,请检查数据。");
                    return jsonObject;
                }

                try {
                    row.getCell(9).setCellType(CellType.NUMERIC);
                    String  preTotal = String.format("%.2f", row.getCell(9).getNumericCellValue());//暂估工程量
                    mesProjectDetail.setPreTotal(String.valueOf(preTotal));
                } catch (Exception e) {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("code",0);
                    jsonObject.put("msg","第"+number+"行,暂估工程量类型不正确,请检查数据。");
                    return jsonObject;
                }

                try {
                    row.getCell(10).setCellType(CellType.NUMERIC);
                    String  price = String.format("%.2f", row.getCell(10).getNumericCellValue());//合同单价
                    mesProjectDetail.setPrice(String.valueOf(price));
                } catch (Exception e) {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("code",0);
                    jsonObject.put("msg","第"+number+"行,合同单价类型不正确,请检查数据。");
                    return jsonObject;
                }

                list.add(mesProjectDetail);
            }











            int j=1;
            for (MesProjectDetail li:list){
                j++;
                MesProjectDetail projectDetails=new MesProjectDetail();
                projectDetails.setWinNo(li.getWinNo().trim());
                projectDetails.setProjectId(li.getProjectId());
                List<MesProjectDetail> list3=mesProjectDetailService.selALL(projectDetails);
                if(list3.size()>0){
                    mesProjectDetailService.updateALL(li.getProjectId());
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("code",0);
                    jsonObject.put("msg","第"+j+"行项目窗号已存在,请检查数据。");
                    return jsonObject;
                }

                MesProjectDetail mesProjectDetail=new MesProjectDetail();
                mesProjectDetail.setId(UUID.randomUUID().toString().replace("-", ""));
                mesProjectDetail.setProjectId(li.getProjectId());
                mesProjectDetail.setWinNo(li.getWinNo().toUpperCase());
                mesProjectDetail.setWinTypeId(li.getWinTypeId()); //窗型
                mesProjectDetail.setNum(li.getNum());//数量
                mesProjectDetail.setBlueprintWidth(li.getBlueprintWidth());//图纸宽
                mesProjectDetail.setDrawingHeight(li.getDrawingHeight());//图纸高
                mesProjectDetail.setWinWidth(li.getWinWidth());//洞口宽
                mesProjectDetail.setWinHeight(li.getWinHeight());//洞口高
                mesProjectDetail.setPreTotal(li.getPreTotal());//暂估工程量，总量
                mesProjectDetail.setPrice(li.getPrice());//合同单价
                mesProjectDetail.setCreateDate(String.valueOf(df.format(new Date())));
                mesProjectDetail.setPartyWinNo(li.getPartyWinNo().toUpperCase());
                mesProjectDetail.setVer("0");
                mesProjectDetail.setItem("0");
                mesProjectDetail.setDel("0");
                mesProjectDetailService.insertSelective(mesProjectDetail);


                MesProjectDetailLog mesProjectDetailLog=new MesProjectDetailLog();
                mesProjectDetailLog.setId(UUID.randomUUID().toString().replace("-", ""));
                mesProjectDetailLog.setWinNo(mesProjectDetail.getWinNo().toUpperCase());//窗号
                mesProjectDetailLog.setWinTypeId(mesProjectDetail.getWinTypeId()); //窗型
                mesProjectDetailLog.setNum(mesProjectDetail.getNum());//数量
                mesProjectDetailLog.setBlueprintWidth(mesProjectDetail.getBlueprintWidth());//图纸宽
                mesProjectDetailLog.setDrawingHeight(mesProjectDetail.getDrawingHeight());//图纸高
                mesProjectDetailLog.setWinWidth(mesProjectDetail.getWinWidth());//洞口宽
                mesProjectDetailLog.setWinHeight(mesProjectDetail.getWinHeight());//洞口高
                mesProjectDetailLog.setPreTotal(mesProjectDetail.getPreTotal());//暂估工程量，总量
                mesProjectDetailLog.setPrice(mesProjectDetail.getPrice());//合同单价
                mesProjectDetailLog.setCreateDate(df.format(new Date()));
                mesProjectDetailLog.setStatus("1");
                mesProjectDetailLog.setProjectId(mesProjectDetail.getProjectId());
                mesProjectDetailLog.setCreateBy(mesUsers.getUsername());
                mesProjectDetailLog.setPartyWinNo(mesProjectDetail.getPartyWinNo().toUpperCase());
                mesProjectDetailLogService.insertSelective(mesProjectDetailLog);
            }
        }catch (Exception e){

            JSONObject jsonObject=new JSONObject();
            jsonObject.put("code",0);
            jsonObject.put("msg","第"+number+"行数据格式错误,请检查数据模板是否按照数据格式准备的数据。");
            e.printStackTrace();
            return jsonObject;


        }

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","导入成功");
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping("sel")
    public  MesProject sel( MesProject mesProject){
        mesProject=  mesProjectService.selectByPrimaryKey(mesProject);
        return mesProject;
    }


}
