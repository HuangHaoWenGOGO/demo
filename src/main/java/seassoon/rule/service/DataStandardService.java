package seassoon.rule.service;

import seassoon.rule.dto.query.DataStandardQuery;
import seassoon.rule.entity.DataStandard;
import seassoon.rule.vo.DataStandardVO;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 数据标准表 服务类
 * </p>
 *
 * @author Wayne
 * @since 2020-07-28
 */
public interface DataStandardService extends IService<DataStandard> {
	/**
	 * 根据数据标准集id，找数据标准记录
	 * @param dataStandardQuery
	 * @return
	 */
	public Page<DataStandard> queryByDataSetId(DataStandardQuery dataStandardQuery);
	
	public boolean saveDataStandardForReviewer(DataStandard dataStandard);
	
	public boolean updateDataStandardForReviewer(DataStandard dataStandard);
	
	public IPage<DataStandardVO> queryDataStandardPage(Page<DataStandardVO> page,String keyword,Integer dataSetId);
	
}
