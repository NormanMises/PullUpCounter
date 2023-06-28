package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 训练成绩对象 tra_grades
 * 
 * @author ldu
 * @date 2023-06-27
 */
public class TraGrades extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 成绩id */
    private Long id;

    /** 训练姓名 */
    @Excel(name = "训练姓名")
    private String traName;

    /** 计数 */
    @Excel(name = "计数")
    private Long traCount;

    /** 登录账号 */
    @Excel(name = "登录账号")
    private String userName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTraName(String traName) 
    {
        this.traName = traName;
    }

    public String getTraName() 
    {
        return traName;
    }
    public void setTraCount(Long traCount) 
    {
        this.traCount = traCount;
    }

    public Long getTraCount() 
    {
        return traCount;
    }
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("traName", getTraName())
            .append("traCount", getTraCount())
            .append("createTime", getCreateTime())
            .append("userName", getUserName())
            .toString();
    }
}
