package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 训练成员信息对象 tra_info
 * 
 * @author ldu
 * @date 2023-06-26
 */
public class TraInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 训练成员id */
    private Long id;

    /** 训练姓名 */
    @Excel(name = "训练姓名")
    private String traInfoName;

    /** 性别 */
    @Excel(name = "性别")
    private String sex;

    /** 年龄 */
    @Excel(name = "年龄")
    private Long age;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phonenumber;

    /** 登录账号 */
    @Excel(name = "登录账号")
    private String userName;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTraInfoName(String traInfoName) 
    {
        this.traInfoName = traInfoName;
    }

    public String getTraInfoName() 
    {
        return traInfoName;
    }
    public void setSex(String sex) 
    {
        this.sex = sex;
    }

    public String getSex() 
    {
        return sex;
    }
    public void setAge(Long age) 
    {
        this.age = age;
    }

    public Long getAge() 
    {
        return age;
    }
    public void setPhonenumber(String phonenumber) 
    {
        this.phonenumber = phonenumber;
    }

    public String getPhonenumber() 
    {
        return phonenumber;
    }
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }
    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("traInfoName", getTraInfoName())
            .append("sex", getSex())
            .append("age", getAge())
            .append("phonenumber", getPhonenumber())
            .append("userName", getUserName())
            .append("password", getPassword())
            .append("createTime", getCreateTime())
            .toString();
    }
}
