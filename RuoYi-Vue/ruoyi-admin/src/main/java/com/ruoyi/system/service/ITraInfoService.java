package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.TraInfo;

/**
 * 训练成员信息Service接口
 * 
 * @author ldu
 * @date 2023-06-26
 */
public interface ITraInfoService 
{
    /**
     * 查询训练成员信息
     * 
     * @param id 训练成员信息主键
     * @return 训练成员信息
     */
    public TraInfo selectTraInfoById(Long id);

    /**
     * 查询训练成员信息列表
     * 
     * @param traInfo 训练成员信息
     * @return 训练成员信息集合
     */
    public List<TraInfo> selectTraInfoList(TraInfo traInfo);

    /**
     * 新增训练成员信息
     * 
     * @param traInfo 训练成员信息
     * @return 结果
     */
    public int insertTraInfo(TraInfo traInfo);

    /**
     * 修改训练成员信息
     * 
     * @param traInfo 训练成员信息
     * @return 结果
     */
    public int updateTraInfo(TraInfo traInfo);

    /**
     * 批量删除训练成员信息
     * 
     * @param ids 需要删除的训练成员信息主键集合
     * @return 结果
     */
    public int deleteTraInfoByIds(Long[] ids);

    /**
     * 删除训练成员信息信息
     * 
     * @param id 训练成员信息主键
     * @return 结果
     */
    public int deleteTraInfoById(Long id);
}
