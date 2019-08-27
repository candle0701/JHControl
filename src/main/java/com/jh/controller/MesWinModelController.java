package com.jh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jh.entity.*;
import com.jh.service.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
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
@RequestMapping("mesWinModel")
public class MesWinModelController {

    @Autowired
    private MesWinModelService mesWinModelService;

    @Autowired
    private MesDictionariesService mesDictionariesService;

    @Autowired
    private MesWinModelCraftService mesWinModelCraftService;

    @Autowired
    private MesProcessQuotaservice mesProcessQuotaservice;

    @Autowired
    private MesMenuButtonService mesMenuButtonService;

    @Autowired
    private MesTaskDetailService mesTaskDetailService;

    @RequestMapping("list")
    public String list(Model model, MesWinModel mesWinModel, String menuId, HttpSession session) {
        MesUsers mesUsers = (MesUsers) session.getAttribute("mesUsers");
        if(mesUsers == null){
            return "redirect:/";
        }else {
            List<MesMenuButton> menuButtonList = mesMenuButtonService.getButtonByRoleIdAndMenuId(mesUsers.getMesRoleUsers().getRoleId(), menuId);
            model.addAttribute("menuButtonList", menuButtonList);
            return "mes_win_model/mes_win_model_list";
        }
    }


    @ResponseBody
    @RequestMapping("pageList")
    public JSONObject list(Model model, MesWinModel mesWinModel, int page, int limit, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            PageHelper.startPage(page, limit);
            List<MesWinModel> list = mesWinModelService.list(mesWinModel);
            PageInfo<MesWinModel> pageInfo = new PageInfo<>(list);
            for (MesWinModel winModel:pageInfo.getList()) {
                if(winModel.getModelStatus().equals("1")){
                    String fileName= winModel.getWinUrl().substring(winModel.getWinUrl().lastIndexOf("/")+1);
                    String url =  request.getScheme()+"://"+ request.getServerName()+":"+ request.getServerPort()+"/uploadFile/"+fileName;
                    winModel.setWinUrl(url);
                }

            }
            jsonObject.put("msg", "");
            jsonObject.put("code", 0);
            jsonObject.put("count", pageInfo.getTotal());
            JSONArray array = JSONArray.parseArray(JSON.toJSONString(pageInfo.getList()));
            jsonObject.put("data", array);

        } catch (Exception e) {
            jsonObject.put("msg", "");
            jsonObject.put("code", 0);
            jsonObject.put("data", "系统错误,请联系管理员");
        }
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping("del")
    public DataInfo del(MesWinModel mesWinModel) {
        DataInfo dataInfo = new DataInfo();
        try {
            mesWinModel = mesWinModelService.find(mesWinModel.getWinId());
            MesTaskDetail mesTaskDetail = new MesTaskDetail();
            mesTaskDetail.setTaskWinType(mesWinModel.getCode());
            mesTaskDetail.setDel("0");
            List<MesTaskDetail> mesTaskDetailList = mesTaskDetailService.find(mesTaskDetail);
            if (mesTaskDetailList.size() > 0) {
                dataInfo.setStatus(false);
                dataInfo.setMsg("作业单正在使用此窗型，请勿删除");
                return dataInfo;
            }
            mesWinModel.setDel("1");
            int count = mesWinModelService.del(mesWinModel);
            MesWinModelCraft mesWinModelCraft = new MesWinModelCraft();
            mesWinModelCraft.setWinModelId(mesWinModel.getWinId());
            List<MesWinModelCraft> list = mesWinModelCraftService.list(mesWinModelCraft);
            for (MesWinModelCraft wmc : list) {
                MesWinModelCraft winModelCraft = new MesWinModelCraft();
                winModelCraft.setId(wmc.getId());
                winModelCraft.setDel("1");
                mesWinModelCraftService.update(winModelCraft);
            }
            if (count > 0) {
                dataInfo.setStatus(true);
                dataInfo.setMsg("删除成功");
            } else {
                dataInfo.setStatus(false);
                dataInfo.setMsg("删除失败");
            }
        } catch (Exception e) {
            dataInfo.setStatus(false);
            dataInfo.setMsg("系统错误,请联系管理员");
        }

        return dataInfo;
    }

    @ResponseBody
    @RequestMapping("verify")
    public DataInfo verify(MesWinModel mesWinModel) {
        DataInfo dataInfo = new DataInfo();
        try {
            List<MesWinModel> list = mesWinModelService.findAll(mesWinModel);
            if (list.size() > 0) {
                if (StringUtils.isNotEmpty(mesWinModel.getWinName())) {
                    dataInfo.setStatus(true);
                    dataInfo.setMsg("窗型名称已存在");
                } else {
                    dataInfo.setStatus(true);
                    dataInfo.setMsg("窗型编号已存在");
                }
            }
        } catch (Exception e) {
            dataInfo.setStatus(true);
            dataInfo.setMsg("系统错误,请联系管理员");
        }
        return dataInfo;
    }


    @RequestMapping("from")
    public String from(Model model, MesWinModel mesWinModel) {
        MesDictionaries mesDictionaries = new MesDictionaries();
        mesDictionaries.setType("WIN_MODEL");
        List<MesDictionaries> dicList = mesDictionariesService.findList(mesDictionaries);
        model.addAttribute("dicList", dicList);
        return "mes_win_model/mes_win_model_from";
    }

    @RequestMapping("edit")
    public String edit(MesWinModel mesWinModel, Model model,HttpServletRequest request ) {
        MesDictionaries mesDictionaries = new MesDictionaries();
        mesDictionaries.setType("WIN_MODEL");
        List<MesDictionaries> dicList = mesDictionariesService.findList(mesDictionaries);
        model.addAttribute("dicList", dicList);
        mesWinModel = mesWinModelService.find(mesWinModel.getWinId());
        if(mesWinModel.getModelStatus().equals("1")) {
            String fileName = mesWinModel.getWinUrl().substring(mesWinModel.getWinUrl().lastIndexOf("/") + 1);
            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/uploadFile/" + fileName;
            mesWinModel.setWinUrl(url);
        }
        model.addAttribute("mesWinModel", mesWinModel);
        return "mes_win_model/mes_win_model_edit";
    }

    @ResponseBody
    @RequestMapping("add")
    public DataInfo add(MesWinModel mesWinModel, HttpSession session) {
        DataInfo dataInfo = new DataInfo();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            MesUsers mesUsers = (MesUsers) session.getAttribute("mesUsers");
            if(mesUsers == null){
                dataInfo.setStatus(false);
                dataInfo.setMsg("获取当前用户失败，请刷新页面.");
                return dataInfo;
            }


                MesDictionaries mesDictionaries = mesDictionariesService.get(mesWinModel.getWinDictionariesId());
                mesWinModel.setCode(mesWinModel.getCode());
                mesWinModel.setWinId(UUID.randomUUID().toString().replace("-", ""));
                mesWinModel.setCreateDate(simpleDateFormat.format(new Date()));
                mesWinModel.setDel("0");

                mesWinModel.setCreator(mesUsers.getUsername());
                int count = mesWinModelService.insertSelective(mesWinModel);

            if (count > 0) {
                /**
                 * 添加窗型工序一级
                 */
                MesWinModelCraft mesWinModelCraft = new MesWinModelCraft();
                mesWinModelCraft.setId(UUID.randomUUID().toString().replace("-", ""));
                mesWinModelCraft.setName(mesWinModel.getWinName());
                mesWinModelCraft.setSort("0");
                mesWinModelCraft.setCreateBy(mesUsers.getUsername());
                mesWinModelCraft.setCreateDate(simpleDateFormat.format(new Date()));
                mesWinModelCraft.setWinModelId(mesWinModel.getWinId());
                mesWinModelCraft.setDel("0");
                mesWinModelCraft.setParentId("0");
                mesWinModelCraft.setCode("1");
                mesWinModelCraft.setNumbers("0");
                mesWinModelCraftService.insertSelective(mesWinModelCraft);
                dataInfo.setStatus(true);
                dataInfo.setMsg("新增成功");
            } else {
                dataInfo.setStatus(false);
                dataInfo.setMsg("新增失败");
            }
        } catch (Exception e) {
            dataInfo.setStatus(false);
            dataInfo.setMsg("系统错误,请联系管理员");
        }
        return dataInfo;
    }

    @ResponseBody
    @RequestMapping("update")
    public DataInfo update(MesWinModel mesWinModel, HttpSession session) {
                    DataInfo dataInfo = new DataInfo();
             try {
                    MesUsers mesUsers = (MesUsers) session.getAttribute("mesUsers");
                    if(mesUsers == null){
                        dataInfo.setStatus(false);
                        dataInfo.setMsg("获取当前用户失败，请刷新页面.");
                        return dataInfo;
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    MesDictionaries mesDictionaries = mesDictionariesService.get(mesWinModel.getWinDictionariesId());
                    MesWinModelCraft mesWinModelCraft = new MesWinModelCraft();
                    mesWinModelCraft.setSort("0");
                    mesWinModelCraft.setParentId("0");
                    mesWinModelCraft.setWinModelId(mesWinModel.getWinId());
                    List<MesWinModelCraft> craftList = mesWinModelCraftService.findList(mesWinModelCraft);
                    mesWinModelCraft.setId(craftList.get(0).getId());

                    mesWinModelCraft.setUpdateBy(mesUsers.getUsername());
                    mesWinModelCraft.setUpdateDate(simpleDateFormat.format(new Date()));
                    mesWinModelCraft.setName(mesWinModel.getWinName());
                    mesWinModelCraftService.update(mesWinModelCraft);


                    mesWinModel.setCode(mesWinModel.getCode());
                    int count = mesWinModelService.del(mesWinModel);

                    if (count > 0) {
                        dataInfo.setStatus(true);
                        dataInfo.setMsg("修改成功");
                    } else {
                        dataInfo.setStatus(false);
                        dataInfo.setMsg("修改失败");
                    }
            } catch (Exception e) {
                dataInfo.setStatus(false);
                dataInfo.setMsg("系统错误,请联系管理员");
            }
        return dataInfo;
    }


    @ResponseBody
    @RequestMapping("findWinModel")
    public DataInfo findWinModel(MesWinModel mesWinModel) {
        List<MesWinModel> list = mesWinModelService.findList(mesWinModel);
        DataInfo dataInfo = new DataInfo();
        if (list.size() > 0) {
            dataInfo.setStatus(true);
            dataInfo.setMsg("成功");
            dataInfo.setList(list);
        } else {
            dataInfo.setStatus(false);
            dataInfo.setMsg("窗型编号不正确,请先创建窗型.");
        }
        return dataInfo;
    }

    /**
     * 模板导出
     *
     * @param session
     * @param response
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("createExcel")
    public String createExcel(HttpSession session, HttpServletResponse response) throws IOException {
        // 新建一个Excel的工作空间
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("工序模板");
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("层次");
        row.createCell(1).setCellValue("名称");
        row.createCell(2).setCellValue("数量");
        row.createCell(3).setCellValue("编码");
        row.createCell(4).setCellValue("父项");
        row.createCell(5).setCellValue("组别");
        row.createCell(6).setCellValue("排序号");
        row.createCell(7).setCellValue("窗型编号");
        /*   row.createCell(8).setCellValue("百分比");*/
        OutputStream output = null;
        try {
            String name = "工序模板";
            output = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment;filename=" + new String(name.getBytes("gbk"), "iso8859-1") + ".xls");
            response.setContentType("application/msexcel");
            workbook.write(output);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 导入
     *
     * @param session
     * @param response
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("importExcel")
    public JSONObject importExcel(HttpSession session, HttpServletResponse response, @RequestParam(value = "file") MultipartFile file) throws IOException {
        String numbers = null;
        int j=1;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String fileName = file.getOriginalFilename();
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            int rowLength = sheet.getLastRowNum() + 1;//总行数
            int colLength = sheet.getRow(0).getLastCellNum();   //总列数

            MesWinModelCraft mesWinModelCraft = new MesWinModelCraft();
            MesWinModel mesWinModel = new MesWinModel();
            for (int i = 1; i < rowLength; i++) {
                Row row = sheet.getRow(i);
                row.getCell(0).setCellType(CellType.STRING);
                String tier = row.getCell(0).getStringCellValue();//层次
                if(tier.trim().length() == 0){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("msg", "导入成功");
                    return jsonObject;
                }
                row.getCell(1).setCellType(CellType.STRING);
                String name = row.getCell(1).getStringCellValue();//名称
                row.getCell(2).setCellType(CellType.STRING);
                String number = row.getCell(2).getStringCellValue();//数量
                row.getCell(3).setCellType(CellType.STRING);
                String code = row.getCell(3).getStringCellValue();//编码

                row.getCell(4).setCellType(CellType.STRING);
                String parentCode = row.getCell(4).getStringCellValue();//父类编号
                row.getCell(5).setCellType(CellType.STRING);
                String group = row.getCell(5).getStringCellValue();//组别
                row.getCell(6).setCellType(CellType.STRING);
                String sort = row.getCell(6).getStringCellValue();//排序
                row.getCell(7).setCellType(CellType.STRING);
                String winCode = row.getCell(7).getStringCellValue();//窗型编号
                mesWinModel.setCode(winCode);
                List<MesWinModel> wml1 = mesWinModelService.findAll(mesWinModel);
                if (wml1.size() > 0 ) {
                    MesWinModelCraft winModelCraft = new MesWinModelCraft();
                    winModelCraft.setWinModelId(wml1.get(0).getWinId());
                    List<MesWinModelCraft> li = mesWinModelCraftService.list(winModelCraft);
                    if (li.size() > 1 && i==1) {
                        MesWinModel mesWinModel1=new MesWinModel();
                        mesWinModel1.setCode(winCode);
                        List<MesWinModel> winModelList = mesWinModelService.findAll(mesWinModel1);
                        mesWinModelCraftService.del(winModelList.get(0).getWinId());
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("code", 0);
                        jsonObject.put("msg", "导入失败,第" + ++i + "行,此窗型已存在工序.");
                        return jsonObject;
                    }
                }

                else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", 0);
                    jsonObject.put("msg", "导入失败,窗型"+winCode+"不存在,请添加窗型"+winCode+"后再导入");
                    return jsonObject;
                }
                if(Integer.valueOf(tier)==3){
                        MesProcessQuota   processQuota=new    MesProcessQuota();
                        processQuota.setId(code);
                        MesProcessQuota mesProcessQuota= mesProcessQuotaservice.find(processQuota);
                        if(mesProcessQuota == null){
                            MesWinModel mesWinModel1=new MesWinModel();
                            mesWinModel1.setCode(winCode);
                            List<MesWinModel> winModelList = mesWinModelService.findAll(mesWinModel1);
                            mesWinModelCraftService.del(winModelList.get(0).getWinId());
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("code", 0);
                            jsonObject.put("msg", "导入失败,第" + ++i + "行,此工序不存在.");
                            return jsonObject;
                        }
                }



                if (i == 1) {
                    mesWinModel.setCode(winCode);
                    List<MesWinModel> wml = mesWinModelService.findAll(mesWinModel);
                    if (wml.size() > 0) {
                        MesWinModelCraft winModelCraft = new MesWinModelCraft();
                        winModelCraft.setWinModelId(wml.get(0).getWinId());
                        List<MesWinModelCraft> li = mesWinModelCraftService.list(winModelCraft);
                        if (li.size() > 1) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("code", 0);
                            jsonObject.put("msg", "导入失败,第" + ++i + "行,此窗型已存在工序.");
                            return jsonObject;
                        }
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("code", 0);
                        jsonObject.put("msg", "导入失败,第" + ++i + "行,此窗型不存在.");
                        return jsonObject;
                    }
                }

                if (numbers != null) {
                    if (!numbers.equals(winCode)) {
                        mesWinModel.setCode(winCode);
                        List<MesWinModel> winModelList = mesWinModelService.findAll(mesWinModel);
                        mesWinModelCraftService.del(winModelList.get(0).getWinId());
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("code", 0);
                        i++;
                        jsonObject.put("msg", "导入失败,第" + ++i + "行,窗型编号不正确");
                        return jsonObject;
                    }
                }
                numbers = winCode;
                if (tier.equals("2")) {
                    mesWinModel.setCode(winCode);
                    List<MesWinModel> winModelList = mesWinModelService.findAll(mesWinModel);
                    if (winModelList.size() == 1) {
                        /**
                         * 根据字典表 查询字典表 2级
                         */
                        MesDictionaries mesDictionaries = new MesDictionaries();
                        mesDictionaries.setType("WIN_PROCESS2");
                        mesDictionaries.setValue(code);
                        List<MesDictionaries> mesDictionariesList = mesDictionariesService.findList(mesDictionaries);


                        /**
                         * 查询窗型的一级菜单
                         */
                        MesWinModelCraft winModelCraft = new MesWinModelCraft();
                        winModelCraft.setWinModelId(winModelList.get(0).getWinId());
                        winModelCraft.setCode("1");
                        List<MesWinModelCraft> list = mesWinModelCraftService.list(winModelCraft);

                        mesWinModelCraft.setId(UUID.randomUUID().toString().replace("-", ""));
                        mesWinModelCraft.setName(mesDictionariesList.get(0).getDicId());// 对应字典表ID
                        mesWinModelCraft.setParentId(list.get(0).getId());//对应的父类
                        mesWinModelCraft.setSort(sort);//排序
                        MesUsers mesUsers = (MesUsers) session.getAttribute("mesUsers");
                        mesWinModelCraft.setCreateBy(mesUsers.getUsername());//创建人 当前用户

                        mesWinModelCraft.setCreateDate(df.format(new Date()));//创建时间
                        mesWinModelCraft.setDel("0");//是否删除   1是 0否
                        mesWinModelCraft.setWinModelId(winModelList.get(0).getWinId());//窗型的ID
                        mesWinModelCraft.setNumbers(number);//数量
                        mesWinModelCraft.setCode(tier);//层次
                        mesWinModelCraftService.insertSelective(mesWinModelCraft);
                    } else {
                        mesWinModelCraftService.del(winModelList.get(0).getWinId());
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("code", 0);
                        i++;
                        jsonObject.put("msg", "导入失败,第" + ++i + "行,窗型编号不正确");
                        return jsonObject;
                    }
                } else if (tier.equals("3")) {
                    /**
                     * 查询窗型
                     */
                    mesWinModel.setCode(winCode);
                    List<MesWinModel> winModelList = mesWinModelService.findAll(mesWinModel);

                    if(group.length()<2){
                        mesWinModelCraftService.del(winModelList.get(0).getWinId());
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("code", 0);
                        i++;
                        jsonObject.put("msg", "导入失败,第" + ++i + "行,组别不正确:例如 下料 01  加工 02等");
                        return jsonObject;
                    }
                    /**
                     * 根据字典表 查询字典表
                     */
                    MesDictionaries mesDictionaries = new MesDictionaries();
                    mesDictionaries.setType("WIN_PROCESS3");
                    mesDictionaries.setValue(group);
                    List<MesDictionaries> mesDictionariesList = mesDictionariesService.findList(mesDictionaries);
                    if(mesDictionariesList.size()==0){
                        mesWinModelCraftService.del(winModelList.get(0).getWinId());
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("code", 0);
                        i++;
                        jsonObject.put("msg", "导入失败,第" + ++i + "行,组别不存在");
                        return jsonObject;
                    }



                    MesDictionaries dic = new MesDictionaries();
                    dic.setType("WIN_PROCESS2");
                    dic.setValue(parentCode);
                    List<MesDictionaries> dicList = mesDictionariesService.findList(dic);


                    MesWinModelCraft mwmc = new MesWinModelCraft();
                    mwmc.setName(dicList.get(0).getDicId());
                    mwmc.setWinModelId(winModelList.get(0).getWinId());
                    List<MesWinModelCraft> mwmcList = mesWinModelCraftService.findAll(mwmc);

                    /**
                     * 查询窗型工序三级
                     */
                    MesWinModelCraft winModelCraft = new MesWinModelCraft();
                    winModelCraft.setName(mesDictionariesList.get(0).getDicId());
                    winModelCraft.setWinModelId(winModelList.get(0).getWinId());
                    winModelCraft.setParentId(mwmcList.get(0).getId());
                    List<MesWinModelCraft> winModelCraftList = mesWinModelCraftService.findAll(winModelCraft);


                    if (winModelCraftList.size() > 0) {

                        /**
                         * 查询对应的工序定额
                         */
                        MesProcessQuota mesProcessQuota = new MesProcessQuota();
                        mesProcessQuota.setCode(code);
                        List<MesProcessQuota> mesProcessQuotaList = mesProcessQuotaservice.findCode(mesProcessQuota);


                        mesWinModelCraft.setId(UUID.randomUUID().toString().replace("-", ""));
                        mesWinModelCraft.setName(mesProcessQuotaList.get(0).getId());// 对应工序定额表ID
                        mesWinModelCraft.setParentId(winModelCraftList.get(0).getId());//父类ID
                        mesWinModelCraft.setSort(sort);//排序
                        MesUsers mesUsers = (MesUsers) session.getAttribute("mesUsers");
                        mesWinModelCraft.setCreateBy(mesUsers.getUsername());//创建人 当前用户
                        mesWinModelCraft.setCreateDate(df.format(new Date()));//创建时间
                        mesWinModelCraft.setDel("0");//是否删除   1是 0否
                        mesWinModelCraft.setWinModelId(winModelList.get(0).getWinId());//窗型的ID
                        mesWinModelCraft.setNumbers(number);//数量
                        mesWinModelCraft.setCode("4");//层次
                        mesWinModelCraftService.insertSelective(mesWinModelCraft);

                    } else {
                        MesDictionaries dictionaries = new MesDictionaries();
                        dictionaries.setType("WIN_PROCESS2");
                        dictionaries.setValue(parentCode);
                        List<MesDictionaries> dictionariesList = mesDictionariesService.findList(dictionaries);

                        MesWinModelCraft modelCraft = new MesWinModelCraft();
                        modelCraft.setName(dictionariesList.get(0).getDicId());
                        modelCraft.setWinModelId(winModelList.get(0).getWinId());
                        List<MesWinModelCraft> modelCraftList = mesWinModelCraftService.findAll(modelCraft);

                        mesWinModelCraft.setId(UUID.randomUUID().toString().replace("-", ""));
                        mesWinModelCraft.setName(mesDictionariesList.get(0).getDicId());// 对应字典表ID
                        mesWinModelCraft.setParentId(modelCraftList.get(0).getId());//父类ID
                        mesWinModelCraft.setSort(sort);//排序
                        MesUsers mesUsers = (MesUsers) session.getAttribute("mesUsers");
                        mesWinModelCraft.setCreateBy(mesUsers.getUsername());//创建人 当前用户
                        mesWinModelCraft.setCreateDate(df.format(new Date()));//创建时间
                        mesWinModelCraft.setDel("0");//是否删除   1是 0否
                        mesWinModelCraft.setWinModelId(winModelList.get(0).getWinId());//窗型的ID
                        mesWinModelCraft.setNumbers("1");//数量
                        mesWinModelCraft.setCode("3");//层次
                        mesWinModelCraftService.insertSelective(mesWinModelCraft);
                        /**
                         * 查询对应的工序定额
                         */
                        MesProcessQuota mesProcessQuota = new MesProcessQuota();
                        mesProcessQuota.setCode(code);
                        List<MesProcessQuota> mesProcessQuotaList = mesProcessQuotaservice.findCode(mesProcessQuota);

                        mesWinModelCraft.setParentId(mesWinModelCraft.getId());//父类ID
                        mesWinModelCraft.setId(UUID.randomUUID().toString().replace("-", ""));
                        mesWinModelCraft.setName(mesProcessQuotaList.get(0).getId());// 对应工序定额表ID
                        mesWinModelCraft.setSort(sort);//排序
                        mesWinModelCraft.setCreateBy(mesUsers.getUsername());//创建人 当前用户
                        mesWinModelCraft.setCreateDate(df.format(new Date()));//创建时间
                        mesWinModelCraft.setDel("0");//是否删除   1是 0否
                        mesWinModelCraft.setWinModelId(winModelList.get(0).getWinId());//窗型的ID
                        mesWinModelCraft.setNumbers(number);//数量
                        mesWinModelCraft.setCode("4");//层次
                        mesWinModelCraftService.insertSelective(mesWinModelCraft);
                    }
                }

            }
        } catch (Exception e) {
            if(j>3){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg", "导入失败,第" + ++j + "行数据格式错误,请检查数据模板是否按照数据格式准备的数据。");
                return jsonObject;
            }else{
                MesWinModel mesWinModel=new MesWinModel();
                mesWinModel.setCode(numbers);
                List<MesWinModel> winModelList = mesWinModelService.findAll(mesWinModel);
                mesWinModelCraftService.del(winModelList.get(0).getWinId());

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg", "导入失败,第" + ++j + "行数据格式错误,请检查数据模板是否按照数据格式准备的数据。");
                return jsonObject;
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", "导入成功");
        return jsonObject;
    }


    /**
     *  导出工序
     * @param session
     * @param response
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("exportExcel")
    public  String exportExcel(HttpSession session, HttpServletResponse response,MesWinModelCraft mesWinModelCraft ) throws IOException{
        // 新建一个Excel的工作空间
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet=workbook.createSheet("工序导出");
        HSSFRow row=sheet.createRow(0);
        row.createCell(0).setCellValue("层次");
        row.createCell(1).setCellValue("名称");
        row.createCell(2).setCellValue("数量");
        row.createCell(3).setCellValue("编码");
        row.createCell(4).setCellValue("父项");
        row.createCell(5).setCellValue("组别");
        row.createCell(6).setCellValue("排序号");
        row.createCell(7).setCellValue("窗型编号");
      /*  row.createCell(8).setCellValue("百分比");
*/
        int i=1;
        List<MesWinModelCraft> mesWinModelCraftList=mesWinModelCraftService.findAll(mesWinModelCraft);
        for (MesWinModelCraft list:mesWinModelCraftList){

            if(list.getCode().equals("1")){
                HSSFRow rows=sheet.createRow(i);
                rows.createCell(0).setCellValue(list.getCode());
                rows.createCell(1).setCellValue(list.getName());
                rows.createCell(2).setCellValue(list.getNumbers());
                MesWinModel  mesWinModel=mesWinModelService.find(list.getWinModelId());
                rows.createCell(3).setCellValue(mesWinModel.getCode());
                rows.createCell(4).setCellValue("无");
                rows.createCell(5).setCellValue("无");
                rows.createCell(6).setCellValue(list.getSort());
                rows.createCell(7).setCellValue(mesWinModel.getCode());
                /*rows.createCell(8).setCellValue("");*/
                i++;
            }else if(list.getCode().equals("2")){
                HSSFRow rows=sheet.createRow(i);
                rows.createCell(0).setCellValue(list.getCode());
                MesDictionaries mesDictionaries=mesDictionariesService.get(list.getName());
                rows.createCell(1).setCellValue(mesDictionaries.getName());
                rows.createCell(2).setCellValue(list.getNumbers());
                rows.createCell(3).setCellValue(mesDictionaries.getValue());


                MesWinModelCraft mesWinModelList=mesWinModelCraftService.find(list.getParentId());
                rows.createCell(4).setCellValue(mesWinModelList.getName());

                rows.createCell(5).setCellValue("无");
                rows.createCell(6).setCellValue(list.getSort());

                MesWinModel  mesWinModel=mesWinModelService.find(list.getWinModelId());
                rows.createCell(7).setCellValue(mesWinModel.getCode());
                rows.createCell(8).setCellValue(list.getPercentage());
                i++;
            }else if(list.getCode().equals("4")){
                HSSFRow rows=sheet.createRow(i);
                rows.createCell(0).setCellValue("3");
                MesProcessQuota MesProcessQuota=new MesProcessQuota();
                MesProcessQuota.setId(list.getName());
                MesProcessQuota=mesProcessQuotaservice.find(MesProcessQuota);
                rows.createCell(1).setCellValue(MesProcessQuota.getName());
                rows.createCell(2).setCellValue(list.getNumbers());
                rows.createCell(3).setCellValue(MesProcessQuota.getCode());


                MesWinModelCraft mesWinModelList=mesWinModelCraftService.find(list.getParentId());

                MesWinModelCraft winModelList=mesWinModelCraftService.find(mesWinModelList.getParentId());

                MesDictionaries mesDictionaries=mesDictionariesService.get(winModelList.getName());
                rows.createCell(4).setCellValue(mesDictionaries.getName());

                MesDictionaries dictionaries=mesDictionariesService.get(mesWinModelList.getName());
                rows.createCell(5).setCellValue(dictionaries.getName());

                rows.createCell(6).setCellValue(list.getSort());

                MesWinModel  mesWinModel=mesWinModelService.find(list.getWinModelId());
                rows.createCell(7).setCellValue(mesWinModel.getCode());
               /* row.createCell(8).setCellValue("");*/
                i++;

            }
        }
        OutputStream output= null;
        try {
            String name="工序导出";
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




}



