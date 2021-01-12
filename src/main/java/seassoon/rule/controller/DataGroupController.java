package seassoon.rule.controller;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import seassoon.rule.dto.query.DataGroupQuery;
import seassoon.rule.entity.Admin;
import seassoon.rule.entity.DataGroup;
import seassoon.rule.entity.DataSet;
import seassoon.rule.exception.CustomException;
import seassoon.rule.exception.CustomExceptionMessage;
import seassoon.rule.service.DataGroupService;
import seassoon.rule.service.DataSetService;
import seassoon.rule.shiro.ShiroUtils;
import seassoon.rule.utils.CopyUtils;
import seassoon.rule.utils.TreeDataJson;
import seassoon.rule.vo.DataGroupVO;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhangqianfeng
 * @since 2020-07-26
 */
@Api(tags = "data-group")
@RestController
@RequestMapping("/data-group")
public class DataGroupController {
	
	@Autowired
	private DataGroupService dataGroupService;
	
	@Autowired
	private DataSetService dataSetService;

	@ApiOperation("数据分组列表")
    @GetMapping
    public Page<DataGroup> query(DataGroupQuery dataGroupQuery) {
		
        return dataGroupService.query(dataGroupQuery);
    }
	
	@SuppressWarnings("unchecked")
	@ApiOperation("数据分组列表关联外键")
    @GetMapping("page")
    public IPage<DataGroupVO> queryIPage(DataGroupQuery dataGroupQuery) {
		
        return dataGroupService.queryDataGroupPage(dataGroupQuery.page(),dataGroupQuery.getKeyword());
    }
	
	
	@GetMapping("{id}")
    public DataGroup get(@PathVariable Integer id) {
        return dataGroupService.getById(id);
    }
	
	@GetMapping("tree")
    public List<TreeDataJson> getDataTree() {
		List<DataGroup> dataGroupList=dataGroupService.list();
		List<TreeDataJson> treeListJson=new ArrayList<TreeDataJson>();
        for(DataGroup dg:dataGroupList){
        	TreeDataJson tt=new TreeDataJson();
        	tt.setId(dg.getId());
        	tt.setLabel(dg.getName());
        	
        	List<DataSet> dataSetList = dataSetService.getDataSetByDataGroupId(dg.getId());
        	List<TreeDataJson> treeListJsonChildren=new ArrayList<TreeDataJson>();
        	for(DataSet ds:dataSetList) {
        		TreeDataJson tc=new TreeDataJson();
        		tc.setId(ds.getId());
        		tc.setLabel(ds.getName());
        		treeListJsonChildren.add(tc);
        	}
        	tt.setChildren(treeListJsonChildren);
        	treeListJson.add(tt);
        }
        
        return treeListJson;
    }
	
	@PostMapping
    public boolean save(@Valid  @RequestBody DataGroup dataGroup) {
		
		Admin currentAdmin = ShiroUtils.currentAdmin();
		System.out.println(currentAdmin.getId());
		dataGroup.setPubAdminId(currentAdmin.getId());
        return dataGroupService.save(dataGroup);
    }

    @ApiOperation("修改")
    @PutMapping("{id}")
    public DataGroup update(@PathVariable Integer id, @Valid @RequestBody DataGroup dataGroup) {
    	
    	DataGroup dbEentity = dataGroupService.getById(id);
        CopyUtils.copyProperties(dataGroup, dbEentity);
        
        dbEentity.setGmtModified(LocalDateTime.now());
        
        Boolean result = dataGroupService.updateById(dbEentity);
    	
        return dbEentity;
    }

    @DeleteMapping("{id}")
    public boolean delete(@PathVariable Integer id) {
    	
    	//判断数据集存在
    	QueryWrapper<DataSet> queryWrapper = new QueryWrapper<>();
    	queryWrapper.eq("data_group_id", id);
    	DataSet ds = dataSetService.getOne(queryWrapper, false);
    	if(ds!=null) {
    		throw new CustomException.BadRequestException(CustomExceptionMessage.HAS_CHILD_DATA);
    	}
    	return dataGroupService.removeById(id);
    }
	
}
