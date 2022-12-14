package com.wqddg.salarys.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveVo implements Serializable {
    private static final long serialVersionUID = 7672846223731449101L;
    private String id;
    /**
     * 企业id
     */
    private String companyId;
    /**
     * 年月
     */
    private String month;
    /**
     * 创建时间
     */
    private String creationTime;
    /**
     * 人工成本
     */
    private BigDecimal artificialCost;
    /**
     * 税前工资
     */
    private BigDecimal grossSalary;
    /**
     * 五险一金
     */
    private BigDecimal fiveInsurances;
}
