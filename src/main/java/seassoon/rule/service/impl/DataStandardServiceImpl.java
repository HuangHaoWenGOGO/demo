package seassoon.rule.service.impl;

import seassoon.rule.dto.query.DataStandardQuery;
import seassoon.rule.entity.Admin;
import seassoon.rule.entity.DataSetReviewerConfig;
import seassoon.rule.entity.DataStandard;
import seassoon.rule.entity.DataStandardReviewRecord;
import seassoon.rule.mapper.DataSetReviewerConfigMapper;
import seassoon.rule.mapper.DataStandardMapper;
import seassoon.rule.mapper.DataStandardReviewRecordMapper;
import seassoon.rule.service.DataStandardService;
import seassoon.rule.shiro.ShiroUtils;
import seassoon.rule.vo.DataStandardVO;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 数据标准表 服务实现类
 * </p>
 *
 * @author Wayne
 * @since 2020-07-28
 */
@Service
@AllArgsConstructor
public class DataStandardServiceImpl extends ServiceImpl<DataStandardMapper, DataStandard>
		implements DataStandardService {

	private DataStandardMapper dataStandardMapper;

	private DataSetReviewerConfigMapper dataSetReviewerConfigMapper;

	private DataStandardReviewRecordMapper dataStandardReviewRecordMapper;

	@Override
	public Page<DataStandard> queryByDataSetId(DataStandardQuery dataStandardQuery) {
		QueryWrapper<DataStandard> queryWrapper = new QueryWrapper<>();
		if (StringUtils.isNotBlank(dataStandardQuery.getKeyword())) {
			queryWrapper.like("name", dataStandardQuery.getKeyword());
		}
		queryWrapper.eq("data_set_id", dataStandardQuery.getDataSetId());
		return this.page(dataStandardQuery.page(), queryWrapper);
	}

	public IPage<DataStandardVO> queryDataStandardPage(Page<DataStandardVO> page, String keyword, Integer dataSetId) {

		return dataStandardMapper.queryDataStandardPage(page, keyword, dataSetId);
	}

	@Override
	@Transactional
	public boolean saveDataStandardForReviewer(DataStandard dataStandard) {
		// 读数据标准对应的数据标准集 对应的审批配置
		int dataSetId = dataStandard.getDataSetId();
		List<DataSetReviewerConfig> dataSetReviewerConfigList = dataSetReviewerConfigMapper.getByDataSetId(dataSetId);
		DataStandardReviewRecord dataStandardReviewRecord = new DataStandardReviewRecord();
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

		// 找出第一位审核人，填上，并生成第一条数据标准记录
		int result = dataStandardMapper.insert(dataStandard);
		int dataStandardId = dataStandard.getId();

		// 处理附件的数据，保存到append file表中 XXX

		// 加审核记录
		dataStandardReviewRecord.setReviewConfig(jsonArray.toJSONString());
		dataStandardReviewRecord.setReviewLevel(dataSetReviewerConfigList.size());
		dataStandardReviewRecord.setDataStandardId(dataStandardId);
		dataStandardReviewRecord.setStatus(0);
//		try {
//	        throw new Exception("抛出异常");
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//	        System.out.println("捕获异常");
//	    }
		dataStandardReviewRecordMapper.insert(dataStandardReviewRecord);

		// 通知TODO

		return true;
	}

	@Override
	@Transactional
	public boolean updateDataStandardForReviewer(DataStandard dataStandard) {
		// 读数据标准对应的数据标准集 对应的审批配置
		int dataSetId = dataStandard.getDataSetId();
		List<DataSetReviewerConfig> dataSetReviewerConfigList = dataSetReviewerConfigMapper.getByDataSetId(dataSetId);
		DataStandardReviewRecord dataStandardReviewRecord = new DataStandardReviewRecord();

		QueryWrapper<DataStandardReviewRecord> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("data_standard_id", dataStandard.getId());

		dataStandardReviewRecord = dataStandardReviewRecordMapper.selectOne(queryWrapper);

		Admin currentAdmin = ShiroUtils.currentAdmin();

		Integer firstCurrentAdminId = 0;
		Integer firstCurrentReviewerOrder = 0;

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
				firstCurrentAdminId = dd.getAdminId();
				firstCurrentReviewerOrder = dd.getReviewOrder();

			}
		}

		// 找出第一位审核人，填上，并生成第一条数据标准记录
		int result = dataStandardMapper.updateById(dataStandard);
		// 处理附件的数据，保存到append file表中

		// 加审核记录
		if (dataStandardReviewRecord == null || dataStandardReviewRecord.getId() < 1) {
			dataStandardReviewRecord = new DataStandardReviewRecord();
			System.out.println(jsonArray.toJSONString());
			dataStandardReviewRecord.setReviewConfig(jsonArray.toJSONString());
			dataStandardReviewRecord.setReviewLevel(dataSetReviewerConfigList.size());
			dataStandardReviewRecord.setDataStandardId(dataStandard.getId());
			dataStandardReviewRecord.setStatus(0);
			dataStandardReviewRecord.setPubAdminId(currentAdmin.getId());

			dataStandardReviewRecord.setCurrentAdminId(firstCurrentAdminId);
			dataStandardReviewRecord.setCurrentReviewerOrder(firstCurrentReviewerOrder);
			dataStandardReviewRecordMapper.insert(dataStandardReviewRecord);
		} else {
			System.out.println(jsonArray.toJSONString());
			dataStandardReviewRecord.setReviewConfig(jsonArray.toJSONString());
			dataStandardReviewRecord.setReviewLevel(dataSetReviewerConfigList.size());
			dataStandardReviewRecord.setDataStandardId(dataStandard.getId());
			dataStandardReviewRecord.setStatus(0);
			dataStandardReviewRecord.setPubAdminId(currentAdmin.getId());

			dataStandardReviewRecord.setCurrentAdminId(firstCurrentAdminId);
			dataStandardReviewRecord.setCurrentReviewerOrder(firstCurrentReviewerOrder);
			dataStandardReviewRecordMapper.updateById(dataStandardReviewRecord);
		}
		// 通知TODO

		return true;
	}

}
