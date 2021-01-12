package seassoon.rule.service.impl;

import seassoon.rule.entity.DataSetReviewerConfig;
import seassoon.rule.mapper.DataSetReviewerConfigMapper;
import seassoon.rule.service.DataSetReviewerConfigService;
import seassoon.rule.vo.DataSetReviewerConfigVO;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 数据标准审核人配置表 服务实现类
 * </p>
 *
 * @author Wayne
 * @since 2020-07-29
 */
@Service
@AllArgsConstructor
public class DataSetReviewerConfigServiceImpl extends ServiceImpl<DataSetReviewerConfigMapper, DataSetReviewerConfig>
		implements DataSetReviewerConfigService {

	private DataSetReviewerConfigMapper dataSetReviewerConfigMapper;

	@Override
	public boolean updateReviewer(Integer dataSetId, List<Integer> adminIdLists) {

		// 删除 dataSetId所有的审核人
		dataSetReviewerConfigMapper.deleteByDataSetId(dataSetId);
		System.out.println("=====++++===");
		// 循环添加一遍
		int reviewerOrder = 0;
		for (Integer adminId : adminIdLists) {
			reviewerOrder++;
			DataSetReviewerConfig dataSetReviewerConfig = new DataSetReviewerConfig();
			dataSetReviewerConfig.setDataSetId(dataSetId);
			dataSetReviewerConfig.setAdminId(adminId);
			dataSetReviewerConfig.setReviewOrder(reviewerOrder);
			dataSetReviewerConfigMapper.insert(dataSetReviewerConfig);
		}
		return reviewerOrder > 0 ? true : false;
	}

	public boolean updateReviewerByDataStandard(Integer dataStandardId, List<Integer> adminIdLists) {

		// 删除 dataSetId所有的审核人
		dataSetReviewerConfigMapper.deleteByDataStandardId(dataStandardId);
		System.out.println("=====++++===");
		// 循环添加一遍
		int reviewerOrder = 0;
		for (Integer adminId : adminIdLists) {
			reviewerOrder++;
			DataSetReviewerConfig dataSetReviewerConfig = new DataSetReviewerConfig();
			dataSetReviewerConfig.setDataStandardId(dataStandardId);
			dataSetReviewerConfig.setAdminId(adminId);
			dataSetReviewerConfig.setReviewOrder(reviewerOrder);
			dataSetReviewerConfigMapper.insert(dataSetReviewerConfig);
		}
		return reviewerOrder > 0 ? true : false;

	}

	public List<DataSetReviewerConfigVO> findDataSetReviewerByDataSetId(Integer dataSetId) {

		return dataSetReviewerConfigMapper.findDataSetReviewerByDataSetId(dataSetId);
	}

	public List<DataSetReviewerConfigVO> findDataSetReviewerByDataStandardId(Integer dataStandardId) {

		return dataSetReviewerConfigMapper.findDataSetReviewerByDataStandardId(dataStandardId);
	}

}
