package com.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import com.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;

import com.entity.ShangpuyuyueEntity;
import com.entity.view.ShangpuyuyueView;

import com.service.ShangpuyuyueService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;

/**
 * 商铺预约
 * 后端接口
 * @author 
 * @email 
 * @date 2022-08-04 21:37:30
 */
@RestController
@RequestMapping("/shangpuyuyue")
public class ShangpuyuyueController {
    @Autowired
    private ShangpuyuyueService shangpuyuyueService;



    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,ShangpuyuyueEntity shangpuyuyue, 
		HttpServletRequest request){

		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("shanghu")) {
			shangpuyuyue.setShanghuzhanghao((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<ShangpuyuyueEntity> ew = new EntityWrapper<ShangpuyuyueEntity>();


		PageUtils page = shangpuyuyueService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shangpuyuyue), params), params));
        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,ShangpuyuyueEntity shangpuyuyue, 
		HttpServletRequest request){
        EntityWrapper<ShangpuyuyueEntity> ew = new EntityWrapper<ShangpuyuyueEntity>();

		PageUtils page = shangpuyuyueService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shangpuyuyue), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( ShangpuyuyueEntity shangpuyuyue){
       	EntityWrapper<ShangpuyuyueEntity> ew = new EntityWrapper<ShangpuyuyueEntity>();
      	ew.allEq(MPUtil.allEQMapPre( shangpuyuyue, "shangpuyuyue")); 
        return R.ok().put("data", shangpuyuyueService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(ShangpuyuyueEntity shangpuyuyue){
        EntityWrapper< ShangpuyuyueEntity> ew = new EntityWrapper< ShangpuyuyueEntity>();
 		ew.allEq(MPUtil.allEQMapPre( shangpuyuyue, "shangpuyuyue")); 
		ShangpuyuyueView shangpuyuyueView =  shangpuyuyueService.selectView(ew);
		return R.ok("查询商铺预约成功").put("data", shangpuyuyueView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ShangpuyuyueEntity shangpuyuyue = shangpuyuyueService.selectById(id);
        return R.ok().put("data", shangpuyuyue);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        ShangpuyuyueEntity shangpuyuyue = shangpuyuyueService.selectById(id);
        return R.ok().put("data", shangpuyuyue);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ShangpuyuyueEntity shangpuyuyue, HttpServletRequest request){
    	shangpuyuyue.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(shangpuyuyue);

        shangpuyuyueService.insert(shangpuyuyue);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody ShangpuyuyueEntity shangpuyuyue, HttpServletRequest request){
    	shangpuyuyue.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(shangpuyuyue);

        shangpuyuyueService.insert(shangpuyuyue);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody ShangpuyuyueEntity shangpuyuyue, HttpServletRequest request){
        //ValidatorUtils.validateEntity(shangpuyuyue);
        shangpuyuyueService.updateById(shangpuyuyue);//全部更新
        return R.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        shangpuyuyueService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 提醒接口
     */
	@RequestMapping("/remind/{columnName}/{type}")
	public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request, 
						 @PathVariable("type") String type,@RequestParam Map<String, Object> map) {
		map.put("column", columnName);
		map.put("type", type);
		
		if(type.equals("2")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			Date remindStartDate = null;
			Date remindEndDate = null;
			if(map.get("remindstart")!=null) {
				Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
				c.setTime(new Date()); 
				c.add(Calendar.DAY_OF_MONTH,remindStart);
				remindStartDate = c.getTime();
				map.put("remindstart", sdf.format(remindStartDate));
			}
			if(map.get("remindend")!=null) {
				Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
				c.setTime(new Date());
				c.add(Calendar.DAY_OF_MONTH,remindEnd);
				remindEndDate = c.getTime();
				map.put("remindend", sdf.format(remindEndDate));
			}
		}
		
		Wrapper<ShangpuyuyueEntity> wrapper = new EntityWrapper<ShangpuyuyueEntity>();
		if(map.get("remindstart")!=null) {
			wrapper.ge(columnName, map.get("remindstart"));
		}
		if(map.get("remindend")!=null) {
			wrapper.le(columnName, map.get("remindend"));
		}

		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("shanghu")) {
			wrapper.eq("shanghuzhanghao", (String)request.getSession().getAttribute("username"));
		}

		int count = shangpuyuyueService.selectCount(wrapper);
		return R.ok().put("count", count);
	}
	







}
