package com.wqddg.audit.service;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: wqddg
 * @ClassName ProcessService
 * @DateTime: 2022/1/20 23:07
 * @remarks : #
 */
@Service
public class ProcessService {

    @Autowired
    private RepositoryService repositoryService;
    public void deployProcess(MultipartFile file, String wqddg) throws Exception {
        //获取上传的文件名称
        String filename = file.getOriginalFilename();
        //2.repositoryService 进行流程部署
        Deployment deploy = repositoryService.createDeployment().addBytes(filename, file.getBytes())
                .tenantId(wqddg)//添加我们的租户i
                .deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    public List getProcessDefinitionList(String companyId) {
        return repositoryService.createProcessDefinitionQuery().processDefinitionTenantId(companyId).latestVersion().list();
    }

    public void suspendProcess(String processKey, String companyId) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processKey)
                .processDefinitionTenantId(companyId).latestVersion().singleResult();
        //挂起状态    流程定义表 act_re_procdef   SUSPENSION_STATE_表示我们的激活状态  1 激活  2挂起
        if (processDefinition.isSuspended()){
            //激活状态
            repositoryService.activateProcessDefinitionByKey(processKey);
        }{
            repositoryService.suspendProcessDefinitionByKey(processKey);
        }
    }
}
