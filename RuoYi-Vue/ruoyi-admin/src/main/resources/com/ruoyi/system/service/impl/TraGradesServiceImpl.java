package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.TraInfo;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.mapper.TraInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TraGradesMapper;
import com.ruoyi.system.domain.TraGrades;
import com.ruoyi.system.service.ITraGradesService;

/**
 * 训练成绩Service业务层处理
 * 
 * @author ldu
 * @date 2023-06-27
 */
@Service
public class TraGradesServiceImpl implements ITraGradesService 
{
    @Autowired
    private TraGradesMapper traGradesMapper;
    @Autowired
    private TraInfoMapper traInfoMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询训练成绩
     * 
     * @param id 训练成绩主键
     * @return 训练成绩
     */
    @Override
    public TraGrades selectTraGradesById(Long id)
    {
        return traGradesMapper.selectTraGradesById(id);
    }

    /**
     * 查询训练成绩列表
     * 
     * @param traGrades 训练成绩
     * @return 训练成绩
     */
    @Override
    public List<TraGrades> selectTraGradesList(TraGrades traGrades)
    {
        return traGradesMapper.selectTraGradesList(traGrades);
    }

    /**
     * 新增训练成绩
     * 
     * @param traGrades 训练成绩
     * @return 结果
     */
    @Override
    public int insertTraGrades(TraGrades traGrades)
    {
        // todo 超级管理员肯定不能训练的啦
        String username = traGrades.getUserName();
//        if (username == "admin") {
//            throw new ServiceException("超级管理员就好好管理，别训练啦！！");
//        }
        TraInfo traInfo = traInfoMapper.selectTraInfoByUserName(username);
        SysUser sysUser1 = sysUserMapper.selectUserByUserName(username);
        // 如果不是训练成员时，先注册
        if (traInfo == null) {
            traInfo.setUserName(sysUser1.getUserName());
            traInfo.setTraInfoName(sysUser1.getNickName());
            traInfo.setPhonenumber(sysUser1.getPhonenumber());
            traInfo.setSex(sysUser1.getSex());
            traInfoMapper.insertTraInfo(traInfo);
        }

        // 先后 训练名、昵称、登录账号
        if (traInfo.getTraInfoName() != null) {
            traGrades.setTraName(traInfo.getTraInfoName());
        } else if(sysUser1.getNickName() != null) {
            traGrades.setTraName(sysUser1.getNickName());
        } else {
            traGrades.setTraName(traGrades.getUserName());
        }
        traGrades.setCreateTime(DateUtils.getNowDate());
        return traGradesMapper.insertTraGrades(traGrades);
    }

    /**
     * 修改训练成绩
     * 
     * @param traGrades 训练成绩
     * @return 结果
     */
    @Override
    public int updateTraGrades(TraGrades traGrades)
    {
        return traGradesMapper.updateTraGrades(traGrades);
    }

    /**
     * 批量删除训练成绩
     * 
     * @param ids 需要删除的训练成绩主键
     * @return 结果
     */
    @Override
    public int deleteTraGradesByIds(Long[] ids)
    {
        return traGradesMapper.deleteTraGradesByIds(ids);
    }

    /**
     * 删除训练成绩信息
     * 
     * @param id 训练成绩主键
     * @return 结果
     */
    @Override
    public int deleteTraGradesById(Long id)
    {
        return traGradesMapper.deleteTraGradesById(id);
    }
}
