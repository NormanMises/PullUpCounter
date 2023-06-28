package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.TraGrades;

/**
 * 训练成绩Service接口
 * 
 * @author ldu
 * @date 2023-06-27
 */
public interface ITraGradesService 
{
    /**
     * 查询训练成绩
     * 
     * @param id 训练成绩主键
     * @return 训练成绩
     */
    public TraGrades selectTraGradesById(Long id);

    /**
     * 查询训练成绩列表
     * 
     * @param traGrades 训练成绩
     * @return 训练成绩集合
     */
    public List<TraGrades> selectTraGradesList(TraGrades traGrades);

    /**
     * 新增训练成绩
     * 
     * @param traGrades 训练成绩
     * @return 结果
     */
    public int insertTraGrades(TraGrades traGrades);

    /**
     * 修改训练成绩
     * 
     * @param traGrades 训练成绩
     * @return 结果
     */
    public int updateTraGrades(TraGrades traGrades);

    /**
     * 批量删除训练成绩
     * 
     * @param ids 需要删除的训练成绩主键集合
     * @return 结果
     */
    public int deleteTraGradesByIds(Long[] ids);

    /**
     * 删除训练成绩信息
     * 
     * @param id 训练成绩主键
     * @return 结果
     */
    public int deleteTraGradesById(Long id);
}
