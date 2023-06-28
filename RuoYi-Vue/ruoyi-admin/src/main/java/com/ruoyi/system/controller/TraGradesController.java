package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.TraGrades;
import com.ruoyi.system.service.ITraGradesService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 训练成绩Controller
 * 
 * @author ldu
 * @date 2023-06-27
 */
@RestController
@RequestMapping("/system/traGrades")
public class TraGradesController extends BaseController
{
    @Autowired
    private ITraGradesService traGradesService;


    /**
     * 查询训练成绩列表
     */
    @PreAuthorize("@ss.hasPermi('system:traGrades:list')")
    @GetMapping("/list")
    public TableDataInfo list(TraGrades traGrades)
    {
        startPage();
        List<TraGrades> list = traGradesService.selectTraGradesList(traGrades);
        return getDataTable(list);
    }

    /**
     * 导出训练成绩列表
     */
    @PreAuthorize("@ss.hasPermi('system:traGrades:export')")
    @Log(title = "训练成绩", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TraGrades traGrades)
    {
        List<TraGrades> list = traGradesService.selectTraGradesList(traGrades);
        ExcelUtil<TraGrades> util = new ExcelUtil<TraGrades>(TraGrades.class);
        util.exportExcel(response, list, "训练成绩数据");
    }

    /**
     * 获取训练成绩详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:traGrades:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(traGradesService.selectTraGradesById(id));
    }

    /**
     * 新增训练成绩
     */
    @PreAuthorize("@ss.hasPermi('system:traGrades:add')")
    @Log(title = "训练成绩", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TraGrades traGrades)
    {
        return toAjax(traGradesService.insertTraGrades(traGrades));
    }

    // 无安全验证
    @Log(title = "训练成绩", businessType = BusinessType.INSERT)
    @PostMapping("/save")
    public AjaxResult save(@RequestBody TraGrades traGrades)
    {
        return toAjax(traGradesService.insertTraGrades(traGrades));
    }

    /**
     * 修改训练成绩
     */
    @PreAuthorize("@ss.hasPermi('system:traGrades:edit')")
    @Log(title = "训练成绩", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TraGrades traGrades)
    {
        return toAjax(traGradesService.updateTraGrades(traGrades));
    }

    /**
     * 删除训练成绩
     */
    @PreAuthorize("@ss.hasPermi('system:traGrades:remove')")
    @Log(title = "训练成绩", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(traGradesService.deleteTraGradesByIds(ids));
    }
}
