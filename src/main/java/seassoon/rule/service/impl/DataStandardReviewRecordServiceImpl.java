package seassoon.rule.service.impl;

import seassoon.rule.dto.DataStandardReviewRecordPageDTO;
import seassoon.rule.dto.query.DataStandardReviewRecordPageDTOQuery;
import seassoon.rule.dto.query.DataStandardReviewRecordQuery;
import seassoon.rule.entity.Admin;
import seassoon.rule.entity.DataSetReviewerConfig;
import seassoon.rule.entity.DataStandard;
import seassoon.rule.entity.DataStandardReviewRecord;
import seassoon.rule.mapper.DataSetReviewerConfigMapper;
import seassoon.rule.mapper.DataStandardMapper;
import seassoon.rule.mapper.DataStandardReviewRecordMapper;
import seassoon.rule.service.DataStandardReviewRecordService;
import seassoon.rule.shiro.ShiroUtils;
import seassoon.rule.vo.DataStandardReviewRecordVO;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 数据标准审核表 服务实现类
 * </p>
 *
 * @author Wayne
 * @since 2020-07-29
 */
@Service
@AllArgsConstructor
public class DataStandardReviewRecordServiceImpl extends ServiceImpl<DataStandardReviewRecordMapper, DataStandardReviewRecord>implements DataStandardReviewRecordService {

	private DataSetReviewerConfigMapper dataSetReviewerConfigMapper;

	private DataStandardReviewRecordMapper dataStandardReviewRecordMapper;

	private DataStandardMapper dataStandardMapper;

	public Page<DataStandardReviewRecord> queryDataStandardReviewRecord(
			DataStandardReviewRecordQuery dataStandardReviewRecordQuery) {

		QueryWrapper<DataStandardReviewRecord> queryWrapper = new QueryWrapper<>();

		Admin currentAdmin = ShiroUtils.currentAdmin();
		System.out.println("=============");
		System.out.println(currentAdmin.getUsername());
		System.out.println(currentAdmin.getId());

		queryWrapper.eq("current_admin_id", currentAdmin.getId()).or().eq("pub_admin_id", currentAdmin.getId());

		Page<DataStandardReviewRecord> pageDataStandardReviewRecord = this.page(dataStandardReviewRecordQuery.page(),
				queryWrapper);

		return this.page(dataStandardReviewRecordQuery.page(), queryWrapper);

	}

	public boolean doRepubById(DataStandardReviewRecord dataStandardReviewRecord) {

		System.out.println("======="+dataStandardReviewRecord.getDataStandardId());
		
		DataStandard dataStandard = dataStandardMapper.selectById(dataStandardReviewRecord.getDataStandardId());
		
		
		System.out.println("======"+dataStandard.getName());

		List<DataSetReviewerConfig> dataSetReviewerConfigList = dataSetReviewerConfigMapper
				.getByDataSetId(dataStandard.getDataSetId());

		// 预设审批的json
		JSONArray jsonArray = new JSONArray();
		for (DataSetReviewerConfig dd : dataSetReviewerConfigList) {
			JSONObject jso = new JSONObject();
			jso.put("id", dd.getId());
			jso.put("admin_id", dd.getAdminId());
			jso.put("data_set_id", dd.getDataSetId());
			jso.put("review_order", dd.getReviewOrder());
			jsonArray.add(jso);
			System.out
					.println(dd.getAdminId() + "|" + dd.getDataSetId() + "|" + dd.getId() + "|" + dd.getReviewOrder());

			if (dd.getReviewOrder() == 1) {
				// 找出第一位审核人，填上，并生成一条一条数据标准记录
				dataStandardReviewRecord.setCurrentAdminId(dd.getAdminId());
				dataStandardReviewRecord.setCurrentReviewerOrder(dd.getReviewOrder());
			}
		}

		dataStandardReviewRecord.setReviewConfig(jsonArray.toJSONString());
		dataStandardReviewRecord.setReviewLevel(dataSetReviewerConfigList.size());
		dataStandardReviewRecord.setStatus(0);
		dataStandardReviewRecord.setReviewReason("");
		dataStandardReviewRecordMapper.updateById(dataStandardReviewRecord);

		return true;
	}
	
	
	public IPage<DataStandardReviewRecordPageDTO> selectDataStandardReviewRecordPageVo(Page<DataStandardReviewRecordPageDTOQuery> page, Integer pub_admin_id,Integer current_admin_id) {
	    
	    return dataStandardReviewRecordMapper.selectDataStandardReviewRecordPageVo(page, pub_admin_id, current_admin_id);
	    
	}
	
	public IPage<DataStandardReviewRecordVO> queryDataStandardReviewPage(Page<DataStandardReviewRecordVO> page,Integer pubAdminId,Integer currentAdminId){
		return dataStandardReviewRecordMapper.queryDataStandardReviewPage(page, pubAdminId, currentAdminId);
	}
	
	public DataStandardReviewRecord getDataStandardReviewRecordByDataStandardId(Integer dataStandardId) {
		
		QueryWrapper<DataStandardReviewRecord> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("data_standard_id", dataStandardId);
		
		return this.getOne(queryWrapper, false);
		
	}

}
