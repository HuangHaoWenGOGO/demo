package seassoon.rule.controller;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiOperation;
import seassoon.rule.dto.query.DataStandardQuery;
import seassoon.rule.entity.Admin;
import seassoon.rule.entity.DataStandard;
import seassoon.rule.entity.DataStandardAppendFile;
import seassoon.rule.exception.CustomException;
import seassoon.rule.exception.CustomExceptionMessage;
import seassoon.rule.service.DataSetReviewerConfigService;
import seassoon.rule.service.DataStandardAppendFileService;
import seassoon.rule.service.DataStandardReviewRecordService;
import seassoon.rule.service.DataStandardService;
import seassoon.rule.shiro.ShiroUtils;
import seassoon.rule.utils.CopyUtils;
import seassoon.rule.vo.DataStandardVO;

/**
 * <p>
 * 数据标准表 前端控制器
 * </p>
 *
 * @author Wayne
 * @since 2020-07-28
 */
@RestController
@RequestMapping("/data-standard")
public class DataStandardController {

	@Autowired
	private DataStandardService dataStandardService;
	
	@Autowired
	private DataStandardAppendFileService dataStandardAppendFileService;
	
	@Autowired
	private DataStandardReviewRecordService dataStandardReviewRecordService;
	
	@Autowired
	private DataSetReviewerConfigService dataSetReviewerConfigService;
	
	@ApiOperation("数据标准列表")
    @GetMapping("datastandards")
    public Page<DataStandard> query(DataStandardQuery dataStandardQuery) {
		
		
		Admin currentAdmin = ShiroUtils.currentAdmin();
		
        return dataStandardService.queryByDataSetId(dataStandardQuery);
    }
	
	@SuppressWarnings("unchecked")
	@ApiOperation("数据标准列表自定义")
    @GetMapping("page")
    public IPage<DataStandardVO> queryPage(DataStandardQuery dataStandardQuery) {

        return dataStandardService.queryDataStandardPage(dataStandardQuery.page(),dataStandardQuery.getKeyword(),dataStandardQuery.getDataSetId());
    }
	
	@ApiOperation("保存数据标准并提交等待审核")
	@PostMapping
	public int save(@Valid @RequestBody DataStandard dataStandard) {
		
		if(dataSetReviewerConfigService.findDataSetReviewerByDataSetId(dataStandard.getDataSetId()).size()<1) {
			
			throw new CustomException.BadRequestException(CustomExceptionMessage.REVIEWER_IS_NOT_EXIST);
		}
		
		Admin currentAdmin = ShiroUtils.currentAdmin();
		dataStandard.setPubAdminId(currentAdmin.getId());
		//设置未完整记录
		dataStandard.setStatus(3);
		dataStandardService.saveDataStandardForReviewer(dataStandard);
		
		return dataStandard.getId();
	}
	
	@ApiOperation("修改数据标准，并提交审核")
    @PutMapping("{id}")
    public DataStandard update(@PathVariable Integer id, @Valid @RequestBody DataStandard dataStandard) {
		Admin currentAdmin = ShiroUtils.currentAdmin();
		DataStandard dbEentity = dataStandardService.getById(id);
        CopyUtils.copyProperties(dataStandard, dbEentity);
        //设为待审核状态
        dbEentity.setStatus(0);
        dbEentity.setGmtModified(LocalDateTime.now());
        dbEentity.setPubAdminId(currentAdmin.getId());
        dbEentity.setId(id);
        Boolean result = dataStandardService.updateDataStandardForReviewer(dbEentity);
    	
        return dbEentity;
    }
	
	@ApiOperation("删除数据标准")
	@DeleteMapping("{id}")
    public boolean delete(@PathVariable Integer id) {
		
		DataStandard ds=dataStandardService.getById(id);
		Admin currentAdmin = ShiroUtils.currentAdmin();

		//删除审核
		Map<String,Object> mrReviewMap= new HashMap<String, Object>();
		mrReviewMap.put("data_standard_id", id);
		dataStandardReviewRecordService.removeByMap(mrReviewMap);
		
		//删除文件
		dataStandardAppendFileService.removeByMap(mrReviewMap);
		
		//清理文件
		
		//清理附属库
		
		//删除记录
		
    	return dataStandardService.removeById(id);
    } 
	
	@GetMapping("{id}")
    public DataStandardVO get(@PathVariable Integer id) {
		
		DataStandard ds=dataStandardService.getById(id);
		DataStandardVO dsv=new DataStandardVO();
		CopyUtils.copyProperties(ds, dsv);
		List<DataStandardAppendFile> ll=dataStandardAppendFileService.getAppendFileByDataStandardId(id);
		dsv.setDataStandardAppendFileList(ll);
		
		dsv.setDataSetReviewerConfigList(dataSetReviewerConfigService.findDataSetReviewerByDataSetId(ds.getDataSetId()));		
        return dsv;
    }
	
	
	/**
	 * 
	 * @param id  data set id
	 * @param adminIdLists
	 */
	@ApiOperation("设置审批")
	@PutMapping("{id}/udpate-datastandard-reviewer")
    public boolean updateDataStandardReviewer(@PathVariable Integer id, @RequestBody List<Integer> adminIdLists) {
		boolean updateReviewResult = false;
		updateReviewResult = dataSetReviewerConfigService.updateReviewer(id, adminIdLists);
		
		return updateReviewResult;
    }
	
	
}
