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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiOperation;
import seassoon.rule.dto.query.DataSetQuery;
import seassoon.rule.entity.Admin;
import seassoon.rule.entity.DataSet;
import seassoon.rule.entity.DataStandard;
import seassoon.rule.exception.CustomException;
import seassoon.rule.exception.CustomExceptionMessage;
import seassoon.rule.service.DataSetReviewerConfigService;
import seassoon.rule.service.DataSetService;
import seassoon.rule.service.DataStandardService;
import seassoon.rule.shiro.ShiroUtils;
import seassoon.rule.utils.CopyUtils;
import seassoon.rule.utils.RandomUtils;
import seassoon.rule.vo.DataSetVO;

/**
 * <p>
 * 数据集表 前端控制器
 * </p>
 *
 * @author zhangqianfeng
 * @since 2020-07-28
 */
@RestController
@RequestMapping("/data-set")
public class DataSetController {

	@Autowired
	private DataSetService dataSetService;

	@Autowired
	private DataStandardService dataStandardService;

	@Autowired
	DataSetReviewerConfigService dataSetReviewerConfigService;

	@ApiOperation("数据集列表")
	@GetMapping("dataset")
	public Page<DataSet> query(DataSetQuery dataSetQuery) {

		return dataSetService.queryByDataGroupId(dataSetQuery);
	}

	@SuppressWarnings("unchecked")
	@ApiOperation("数据集列表自定义")
	@GetMapping("page")
	public IPage<DataSetVO> queryPage(DataSetQuery dataSetQuery) {

		// 自动统计数据集，随机刷统计 临时
		if (RandomUtils.genNum(0, 20) == 10) {
			dataSetService.updateDataStandardNumber();
		}
		IPage<DataSetVO> page = dataSetService.queryDataSetPage(dataSetQuery.page(), dataSetQuery.getKeyword(),
				dataSetQuery.getDataGroupId());

		return page;
	}

	@PostMapping
	public int save(@Valid @RequestBody DataSet dataSet) {
		Admin currentAdmin = ShiroUtils.currentAdmin();
		dataSet.setPubAdminId(currentAdmin.getId());
		dataSetService.save(dataSet);

		return dataSet.getId();
	}

	@ApiOperation("修改")
	@PutMapping("{id}")
	public DataSet update(@PathVariable Integer id, @Valid @RequestBody DataSet dataSet) {
		System.out.println("======================");
		System.out.println(dataSet.getDataGroupId());
		DataSet dbEentity = dataSetService.getById(id);
		CopyUtils.copyProperties(dataSet, dbEentity);
		
		dbEentity.setGmtModified(LocalDateTime.now());
		
		Boolean result = dataSetService.updateById(dbEentity);

		return dbEentity;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("{id}")
	public boolean delete(@PathVariable Integer id) {

		// 判断数据标准存在
		QueryWrapper<DataStandard> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("data_set_id", id);
		DataStandard ds = dataStandardService.getOne(queryWrapper, false);
		if (ds != null) {
			throw new CustomException.BadRequestException(CustomExceptionMessage.HAS_CHILD_DATA);
		}
		
		dataSetService.removeById(id);

		// 删除 reviewer配置
		Map<String, Object> mrReviewMap = new HashMap<String, Object>();
		mrReviewMap.put("data_set_id", id);
		dataSetReviewerConfigService.removeByMap(mrReviewMap);
		return dataSetService.removeById(id);
	}

	@GetMapping("{id}")
	public DataSetVO get(@PathVariable Integer id) {

		DataSet dataSet = dataSetService.getById(id);
		DataSetVO dataSetVO = new DataSetVO();
		CopyUtils.copyProperties(dataSet, dataSetVO);

		dataSetVO.setDataSetReviewerConfigList(dataSetReviewerConfigService.findDataSetReviewerByDataSetId(id));

		return dataSetVO;
	}

	/**
	 * 
	 * @param id           data set id
	 * @param adminIdLists
	 */
	@ApiOperation("设置审批")
	@PutMapping("{id}/udpate-dataset-reviewer")
	public boolean updateDataSetReviewer(@PathVariable Integer id, @RequestBody List<Integer> adminIdLists) {

		return dataSetReviewerConfigService.updateReviewer(id, adminIdLists);
	}

}
