package seassoon.rule.service;

import seassoon.rule.dto.query.DataGroupQuery;
import seassoon.rule.entity.DataGroup;
import seassoon.rule.vo.DataGroupVO;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangqianfeng
 * @since 2020-07-26
 */
public interface DataGroupService extends IService<DataGroup> {

	Page<DataGroup> query(DataGroupQuery dataGroupQuery);
	
	public IPage<DataGroupVO> queryDataGroupPage(Page<DataGroupVO> page,String keyword);
}
