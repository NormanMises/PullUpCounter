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
import com.ruoyi.system.domain.TraInfo;
import com.ruoyi.system.service.ITraInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 训练成员信息Controller
 * 
 * @author ldu
 * @date 2023-06-26
 */
@RestController
@RequestMapping("/system/traInfo")
public class TraInfoController extends BaseController
{
    @Autowired
    private ITraInfoService traInfoService;

    /**
     * 查询训练成员信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:traInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(TraInfo traInfo)
    {
        startPage();
        List<TraInfo> list = traInfoService.selectTraInfoList(traInfo);
        return getDataTable(list);
    }

    /**
     * 导出训练成员信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:traInfo:export')")
    @Log(title = "训练成员信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TraInfo traInfo)
    {
        List<TraInfo> list = traInfoService.selectTraInfoList(traInfo);
        ExcelUtil<TraInfo> util = new ExcelUtil<TraInfo>(TraInfo.class);
        util.exportExcel(response, list, "训练成员信息数据");
    }

    /**
     * 获取训练成员信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:traInfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(traInfoService.selectTraInfoById(id));
    }

    /**
     * 新增训练成员信息
     */
    @PreAuthorize("@ss.hasPermi('system:traInfo:add')")
    @Log(title = "训练成员信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TraInfo traInfo)
    {
        return toAjax(traInfoService.insertTraInfo(traInfo));
    }

    /**
     * 修改训练成员信息
     */
    @PreAuthorize("@ss.hasPermi('system:traInfo:edit')")
    @Log(title = "训练成员信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TraInfo traInfo)
    {
        return toAjax(traInfoService.updateTraInfo(traInfo));
    }

    /**
     * 删除训练成员信息
     */
    @PreAuthorize("@ss.hasPermi('system:traInfo:remove')")
    @Log(title = "训练成员信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(traInfoService.deleteTraInfoByIds(ids));
    }
}
