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

import com.entity.TongzhitixingEntity;
import com.entity.view.TongzhitixingView;

import com.service.TongzhitixingService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;

/**
 * 通知提醒
 * 后端接口
 * @author 
 * @email 
 * @date 2022-08-04 21:37:30
 */
@RestController
@RequestMapping("/tongzhitixing")
public class TongzhitixingController {
    @Autowired
    private TongzhitixingService tongzhitixingService;



    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,TongzhitixingEntity tongzhitixing, 
		HttpServletRequest request){

		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("shanghu")) {
			tongzhitixing.setShanghuzhanghao((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<TongzhitixingEntity> ew = new EntityWrapper<TongzhitixingEntity>();


		PageUtils page = tongzhitixingService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, tongzhitixing), params), params));
        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,TongzhitixingEntity tongzhitixing, 
		HttpServletRequest request){
        EntityWrapper<TongzhitixingEntity> ew = new EntityWrapper<TongzhitixingEntity>();

		PageUtils page = tongzhitixingService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, tongzhitixing), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( TongzhitixingEntity tongzhitixing){
       	EntityWrapper<TongzhitixingEntity> ew = new EntityWrapper<TongzhitixingEntity>();
      	ew.allEq(MPUtil.allEQMapPre( tongzhitixing, "tongzhitixing")); 
        return R.ok().put("data", tongzhitixingService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(TongzhitixingEntity tongzhitixing){
        EntityWrapper< TongzhitixingEntity> ew = new EntityWrapper< TongzhitixingEntity>();
 		ew.allEq(MPUtil.allEQMapPre( tongzhitixing, "tongzhitixing")); 
		TongzhitixingView tongzhitixingView =  tongzhitixingService.selectView(ew);
		return R.ok("查询通知提醒成功").put("data", tongzhitixingView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        TongzhitixingEntity tongzhitixing = tongzhitixingService.selectById(id);
        return R.ok().put("data", tongzhitixing);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        TongzhitixingEntity tongzhitixing = tongzhitixingService.selectById(id);
        return R.ok().put("data", tongzhitixing);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody TongzhitixingEntity tongzhitixing, HttpServletRequest request){
    	tongzhitixing.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(tongzhitixing);

        tongzhitixingService.insert(tongzhitixing);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody TongzhitixingEntity tongzhitixing, HttpServletRequest request){
    	tongzhitixing.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(tongzhitixing);

        tongzhitixingService.insert(tongzhitixing);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody TongzhitixingEntity tongzhitixing, HttpServletRequest request){
        //ValidatorUtils.validateEntity(tongzhitixing);
        tongzhitixingService.updateById(tongzhitixing);//全部更新
        return R.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        tongzhitixingService.deleteBatchIds(Arrays.asList(ids));
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
		
		Wrapper<TongzhitixingEntity> wrapper = new EntityWrapper<TongzhitixingEntity>();
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

		int count = tongzhitixingService.selectCount(wrapper);
		return R.ok().put("count", count);
	}
	







}
