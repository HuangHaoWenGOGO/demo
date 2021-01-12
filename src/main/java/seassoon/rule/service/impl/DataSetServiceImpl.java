package seassoon.rule.service.impl;

import seassoon.rule.dto.query.DataSetQuery;
import seassoon.rule.entity.DataSet;
import seassoon.rule.entity.DataStandard;
import seassoon.rule.mapper.DataSetMapper;
import seassoon.rule.mapper.DataStandardMapper;
import seassoon.rule.service.DataSetService;
import seassoon.rule.vo.DataSetVO;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 数据集表 服务实现类
 * </p>
 *
 * @author zhangqianfeng
 * @since 2020-07-28
 */
@Service
@AllArgsConstructor
public class DataSetServiceImpl extends ServiceImpl<DataSetMapper, DataSet> implements DataSetService {
	
	private DataSetMapper dataSetMapper;
	
	private DataStandardMapper dataStandardMapper;

	@Override
	public List<DataSet> getDataSetByDataGroupId(Integer dataGroupId) {
		QueryWrapper<DataSet> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("data_group_id", dataGroupId);
		return this.list(queryWrapper);
	}
	
	@Override
    public Page<DataSet> queryByDataGroupId(DataSetQuery dataSetQuery) {
        QueryWrapper<DataSet> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(dataSetQuery.getKeyword())) {
            queryWrapper.like("name", dataSetQuery.getKeyword());
        }
        queryWrapper.eq("data_group_id", dataSetQuery.getDataGroupId());
        return this.page(dataSetQuery.page(), queryWrapper);
    }
	
	public IPage<DataSetVO> queryDataSetPage(Page<DataSetVO> page,String keyword,Integer dataGroupId){
		
		
		return dataSetMapper.queryDataSetPage(page, keyword, dataGroupId);
	}
	
	
	/**
	 * 批量更新数据标准集下数据标准的数量
	 */
	public void updateDataStandardNumber() {
		
		QueryWrapper<DataSet> queryWrapper = new QueryWrapper<>();
		List<DataSet> dataSetList = dataSetMapper.selectList(queryWrapper);
		
		for(DataSet ds:dataSetList) {
			
			ds.setDataStandardNumber(this.getDataStandardNumberByDataSetId(ds.getId()));
			dataSetMapper.updateById(ds);
		}
		
	}
	
	/**
	 * 批量更新数据标准集下数据标准的数量
	 */
	public void updateDataStandardNumberByDataSetId(Integer dataSetId) {
		
		DataSet ds=dataSetMapper.selectById(dataSetId);
		ds.setDataStandardNumber(this.getDataStandardNumberByDataSetId(dataSetId));
		dataSetMapper.updateById(ds);
		
	}
	
	/**
	 *  根据datasetid 获取数据标准集下数据标准的数量
	 */
	public int getDataStandardNumberByDataSetId(Integer dataSetId) {
		
		QueryWrapper<DataStandard> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("data_set_id", dataSetId);
		
		return dataStandardMapper.selectCount(queryWrapper);
	}

}
