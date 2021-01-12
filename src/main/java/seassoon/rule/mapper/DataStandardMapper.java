package seassoon.rule.mapper;

import seassoon.rule.entity.DataStandard;
import seassoon.rule.vo.DataStandardVO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 数据标准表 Mapper 接口
 * </p>
 *
 * @author Wayne
 * @since 2020-07-28
 */
public interface DataStandardMapper extends BaseMapper<DataStandard> {
	
	IPage<DataStandardVO> queryDataStandardPage(Page<DataStandardVO> page,String keyword,Integer dataSetId);

}
