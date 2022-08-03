package com.wqddg;

import com.wqddg.audit.AuditApplication;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @Author: wqddg
 * @ClassName Activiti
 * @DateTime: 2022/1/20 23:34
 * @remarks : #
 */
@SpringBootTest(classes = AuditApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class Activiti {

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 查询我们所有的流程对象
     */
    @Test
    public void findAll(){
        //1.获取流程定义查询对象
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        //2.增加查询条件
        processDefinitionQuery.processDefinitionTenantId("wqddg");
        //3.查询
        List<ProcessDefinition> list = processDefinitionQuery.latestVersion().list();
        for (ProcessDefinition processDefinition : list) {
            System.out.println(processDefinition.getId());
            System.out.println(processDefinition.getKey());
            System.out.println(processDefinition.getTenantId());
        }
    }

    /**
     * 流程的挂起和激活
     */
    @Test
    public void start_stop_Acitviti(){
        //挂起状态    流程定义表 act_re_procdef   SUSPENSION_STATE_表示我们的激活状态  1 激活  2挂起
        repositoryService.suspendProcessDefinitionByKey("wqddg_Springboot");
        //激活状态
        repositoryService.activateProcessDefinitionByKey("wqddg_Springboot");
    }


}
