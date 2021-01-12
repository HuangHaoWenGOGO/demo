package seassoon.rule.service;

import seassoon.rule.dto.DataStandardReviewRecordPageDTO;
import seassoon.rule.dto.query.DataStandardReviewRecordPageDTOQuery;
import seassoon.rule.dto.query.DataStandardReviewRecordQuery;
import seassoon.rule.entity.DataStandardReviewRecord;
import seassoon.rule.vo.DataStandardReviewRecordVO;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 数据标准审核表 服务类
 * </p>
 *
 * @author Wayne
 * @since 2020-07-29
 */
public interface DataStandardReviewRecordService extends IService<DataStandardReviewRecord> {

	/**
	 * 根据数据标准的发布人，或者待审核人，去审批
	 * @param dataStandardQuery
	 * @return
	 */
	public Page<DataStandardReviewRecord> queryDataStandardReviewRecord(DataStandardReviewRecordQuery dataStandardReviewRecordQuery);
	
	public boolean doRepubById(DataStandardReviewRecord dataStandardReviewRecord);
	
	public IPage<DataStandardReviewRecordPageDTO> selectDataStandardReviewRecordPageVo(Page<DataStandardReviewRecordPageDTOQuery> page, Integer pub_admin_id,Integer current_admin_id);
	
	public IPage<DataStandardReviewRecordVO> queryDataStandardReviewPage(Page<DataStandardReviewRecordVO> page,Integer pubAdminId,Integer currentAdminId);
	
}
