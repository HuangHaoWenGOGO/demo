package seassoon.rule.mapper;

import seassoon.rule.entity.DataGroup;
import seassoon.rule.vo.DataGroupVO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangqianfeng
 * @since 2020-07-26
 */
public interface DataGroupMapper extends BaseMapper<DataGroup> {

	IPage<DataGroupVO> queryDataGroupPage(Page<DataGroupVO> page,String keyword);
	
}
