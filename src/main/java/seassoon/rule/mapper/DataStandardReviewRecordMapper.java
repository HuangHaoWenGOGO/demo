package seassoon.rule.mapper;

import seassoon.rule.dto.DataStandardReviewRecordPageDTO;
import seassoon.rule.entity.DataStandardReviewRecord;
import seassoon.rule.vo.DataStandardReviewRecordVO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 数据标准审核表 Mapper 接口
 * </p>
 *
 * @author Wayne
 * @since 2020-07-29
 */
public interface DataStandardReviewRecordMapper extends BaseMapper<DataStandardReviewRecord> {
	
	
	IPage<DataStandardReviewRecordPageDTO> selectDataStandardReviewRecordPageVo(Page<?> page, Integer pub_admin_id,Integer current_admin_id);
	
	IPage<DataStandardReviewRecordVO> queryDataStandardReviewPage(Page<DataStandardReviewRecordVO> page,Integer pubAdminId,Integer currentAdminId);
}
