package seassoon.rule.mapper;

import seassoon.rule.entity.DataSetReviewerConfig;
import seassoon.rule.vo.DataSetReviewerConfigVO;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 数据标准审核人配置表 Mapper 接口
 * </p>
 *
 * @author Wayne
 * @since 2020-07-29
 */
public interface DataSetReviewerConfigMapper extends BaseMapper<DataSetReviewerConfig> {
	
	@Delete("delete from ds_data_set_reviewer_config where data_set_id=#{dataSetId} ")
	int deleteByDataSetId(Integer dataSetId);
	
	@Delete("delete from ds_data_set_reviewer_config where data_standard_id=#{dataStandardId} ")
	int deleteByDataStandardId(Integer dataStandardId);
	
	@Select("select * from ds_data_set_reviewer_config where data_set_id=#{dataSetId} order by review_order asc ")
	List<DataSetReviewerConfig> getByDataSetId(Integer dataSetId);
	
	List<DataSetReviewerConfigVO> findDataSetReviewerByDataSetId(Integer dataSetId);
	
	List<DataSetReviewerConfigVO> findDataSetReviewerByDataStandardId(Integer dataStandardId);

}
