package com.wqddg.salarys.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipsVo implements Serializable {
    private static final long serialVersionUID = 28138009041555437L;
    /**
     * 日期范围
     */
    private String dateRange;
    /**
     * 入职
     */
    private Long worksCount;
    /**
     * 离职
     */
    private Long leavesCount;
    /**
     * 调薪
     */
    private Long adjustCount;
    /**
     * 未定级
     */
    private Long unGradingCount;
    /**
     * 月份
     */
    private Integer month;
    /**
     * 大写月份
     */
    private String strMonth;
}
