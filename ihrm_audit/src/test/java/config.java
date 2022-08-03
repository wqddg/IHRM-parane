import com.wqddg.audit.AuditApplication;
import com.wqddg.audit.dao.ProcUserGroupDao;
import com.wqddg.audit.entity.ProcUserGroup;
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
 * @ClassName config
 * @DateTime: 2022/1/18 22:33
 * @remarks : #
 */
@SpringBootTest(classes = AuditApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class config {

    @Autowired
    private ProcUserGroupDao procUserGroupDao;

    @Test
    public void test(){
        List<ProcUserGroup> all = procUserGroupDao.findAll();
        System.out.println(all.size());
    }

    @Autowired
    private RepositoryService repositoryService;

    @Test
    public void test_2(){
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        System.out.println(list.size());
    }

}
