package seassoon.rule.service;

import seassoon.rule.entity.DataSetReviewerConfig;
import seassoon.rule.vo.DataSetReviewerConfigVO;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 数据标准审核人配置表 服务类
 * </p>
 *
 * @author Wayne
 * @since 2020-07-29
 */
public interface DataSetReviewerConfigService extends IService<DataSetReviewerConfig> {
	
	boolean updateReviewer(Integer dataSetId,List<Integer> adminIdLists);
	
	boolean updateReviewerByDataStandard(Integer dataStandardId,List<Integer> adminIdLists);
	
	List<DataSetReviewerConfigVO> findDataSetReviewerByDataSetId(Integer dataSetId);
	
	List<DataSetReviewerConfigVO> findDataSetReviewerByDataStandardId(Integer dataStandardId);

}
