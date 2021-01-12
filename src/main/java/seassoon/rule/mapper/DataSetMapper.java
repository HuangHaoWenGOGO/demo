package seassoon.rule.mapper;

import seassoon.rule.entity.DataSet;
import seassoon.rule.vo.DataSetVO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 数据集表 Mapper 接口
 * </p>
 *
 * @author zhangqianfeng
 * @since 2020-07-28
 */
public interface DataSetMapper extends BaseMapper<DataSet> {

	// 根据角色获取用户信息列表——使用
//		@Select("select name as name,id as id "
//				+ "from ds_data_set where data_group_id = #{dataGroupId}")
//		public List<DataSet> getDataSetByDataGroupId(String dataGroupId);
	IPage<DataSetVO> queryDataSetPage(Page<DataSetVO> page,String keyword,Integer dataGroupId);
}
