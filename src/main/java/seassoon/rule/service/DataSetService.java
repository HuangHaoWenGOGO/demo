package seassoon.rule.service;

import seassoon.rule.dto.query.DataSetQuery;
import seassoon.rule.entity.DataSet;
import seassoon.rule.vo.DataSetVO;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 数据集表 服务类
 * </p>
 *
 * @author zhangqianfeng
 * @since 2020-07-28
 */
public interface DataSetService extends IService<DataSet> {

	/**
	 * 根据数据组id找数据标准集
	 * @param dataGroupId
	 * @return
	 */
	public List<DataSet> getDataSetByDataGroupId(Integer dataGroupId);
	
	/**
	 * 根据数据组id找数据标准集
	 * @param dataSetQuery
	 * @return
	 */
	public Page<DataSet> queryByDataGroupId(DataSetQuery dataSetQuery);
	
	
	public IPage<DataSetVO> queryDataSetPage(Page<DataSetVO> page,String keyword,Integer dataGroupId);
	
	/**
	 * 批量更新数据标准集下数据标准的数量
	 */
	public void updateDataStandardNumber();
	
	/**
	 * 批量更新数据标准集下数据标准的数量
	 */
	public void updateDataStandardNumberByDataSetId(Integer dataSetId);
	
	/**
	 * datasetid来更新数据标准集下数据标准的数量
	 */
	public int getDataStandardNumberByDataSetId(Integer dataSetId);
	
}
