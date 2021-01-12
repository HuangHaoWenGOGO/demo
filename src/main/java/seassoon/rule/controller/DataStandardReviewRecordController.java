package seassoon.rule.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiOperation;
import seassoon.rule.dto.query.DataStandardReviewRecordPageDTOQuery;
import seassoon.rule.dto.query.DataStandardReviewRecordQuery;
import seassoon.rule.entity.Admin;
import seassoon.rule.entity.DataStandard;
import seassoon.rule.entity.DataStandardAppendFile;
import seassoon.rule.entity.DataStandardReviewRecord;
import seassoon.rule.exception.CustomException;
import seassoon.rule.exception.CustomExceptionMessage;
import seassoon.rule.service.DataStandardAppendFileService;
import seassoon.rule.service.DataStandardReviewRecordService;
import seassoon.rule.service.DataStandardService;
import seassoon.rule.shiro.ShiroUtils;
import seassoon.rule.utils.CopyUtils;
import seassoon.rule.vo.DataStandardReviewRecordVO;

/**
 * <p>
 * 数据标准审核表 前端控制器
 * </p>
 *
 * @author Wayne
 * @since 2020-07-29
 */
@RestController
@RequestMapping("/data-standard-review-record")
public class DataStandardReviewRecordController {

	@Autowired
	private DataStandardReviewRecordService dataStandardReviewRecordService;

	@Autowired
	private DataStandardService dataStandardService;
	
	@Autowired
	private DataStandardAppendFileService dataStandardAppendFileService;

	@ApiOperation("数据标准待审核列表")
	@GetMapping
	public Page<DataStandardReviewRecord> query(DataStandardReviewRecordQuery dataStandardReviewRecordQuery) {

		return dataStandardReviewRecordService.queryDataStandardReviewRecord(dataStandardReviewRecordQuery);
	}

	@ApiOperation("审核通过")
	@PutMapping("{id}/pass")
	public void reviewPass(@PathVariable Integer id) {

		// 获取当前审核记录，审核人，比对
		DataStandardReviewRecord dataStandardReviewRecordDetail = dataStandardReviewRecordService.getById(id);
		Admin currentAdmin = ShiroUtils.currentAdmin();
		if (dataStandardReviewRecordDetail.getCurrentAdminId() != currentAdmin.getId()) {
			// 不是本人 ，报错
			throw new CustomException.ServiceException(
					CustomExceptionMessage.USERNAME_VIEWER_RECORD_CURRENT_ADMIN_NOT_RIGHT.getMessage());
		}

		// 符合本人，待审核状态，并且是待审核本人，拉出审核配置，找下个人
		System.out.println(dataStandardReviewRecordDetail.getReviewConfig());
		JSONArray reviewConfigJsonArray = JSONArray.parseArray(dataStandardReviewRecordDetail.getReviewConfig());
		// reviewPos 审核的位置：0有下一位，1已是最后一位
		int reviewPos = 0;
		int next_admin_id = 0;
		int next_reviewer_order = 0;
		for (int i = 0; i < reviewConfigJsonArray.size(); i++) {
			// 通过数组下标取到object，使用强转转为JSONObject，之后进行操作
			JSONObject object = (JSONObject) reviewConfigJsonArray.get(i);
			System.out.println(object.getString("admin_id"));
			if (object.getIntValue("admin_id") == currentAdmin.getId()) {
				// 找到我
				System.out.println("================================");
				System.out.println(object.getIntValue("admin_id"));
				System.out.println(object.getIntValue("review_order"));
				if (object.getIntValue("review_order") == reviewConfigJsonArray.size()) {
					//// 如果已是最后一位，完成通过，修改DataStandardReviewRecord状态，修改dataStandard状态
					reviewPos = 1;
					next_admin_id = dataStandardReviewRecordDetail.getCurrentAdminId();
					next_reviewer_order = dataStandardReviewRecordDetail.getCurrentReviewerOrder();
					System.out.println("如果已是最后一位，完成通过，修改DataStandardReviewRecord状态，修改dataStandard状态");
					break;
				} else {
					// 设置下一位
					JSONObject nextObject = (JSONObject) reviewConfigJsonArray.get(i + 1);
					next_admin_id = nextObject.getIntValue("admin_id");
					next_reviewer_order = nextObject.getIntValue("review_order");
					System.out.println("设置下一位");
					break;
				}

			}
		}
		// 如果已是最后一位，完成通过，修改DataStandardReviewRecord状态，修改dataStandard状态
		if (reviewPos == 1) {
			// 通知发布者，已通过
			// 修改review record 并修改data standard的状态 显示
			DataStandard dataStandard = dataStandardService.getById(dataStandardReviewRecordDetail.getDataStandardId());
			dataStandardReviewRecordDetail.setStatus(1);
			dataStandard.setStatus(1);
			dataStandardService.updateById(dataStandard);
			dataStandardReviewRecordDetail.setId(id);
			dataStandardReviewRecordDetail.setGmtModified(LocalDateTime.now());
			dataStandardReviewRecordService.updateById(dataStandardReviewRecordDetail);

			// TODO
		}

		// 如果不是最后一位，更换下一位，修改current admin 并通知他
		if (reviewPos == 0) {
			// 通知 新审核人
			// 更新review record
			dataStandardReviewRecordDetail.setCurrentAdminId(next_admin_id);
			dataStandardReviewRecordDetail.setCurrentReviewerOrder(next_reviewer_order);
			dataStandardReviewRecordDetail.setId(id);
			dataStandardReviewRecordDetail.setGmtModified(LocalDateTime.now());
			dataStandardReviewRecordService.updateById(dataStandardReviewRecordDetail);

		}
	}

	@ApiOperation("审核不通过")
	@PutMapping("{id}/notpass")
	public void reviewNotPass(@PathVariable Integer id,
			@RequestBody DataStandardReviewRecord dataStandardReviewRecord) {
		// 获取当前审核记录，审核人，比对
		DataStandardReviewRecord dataStandardReviewRecordDetail = dataStandardReviewRecordService.getById(id);
		Admin currentAdmin = ShiroUtils.currentAdmin();
		System.out.println(currentAdmin.getId());
		if (dataStandardReviewRecordDetail.getCurrentAdminId() != currentAdmin.getId()) {
			// 不是本人 ，报错
			throw new CustomException.ServiceException(
					CustomExceptionMessage.USERNAME_VIEWER_RECORD_CURRENT_ADMIN_NOT_RIGHT.getMessage());
		}

		dataStandardReviewRecordDetail.setReviewReason(dataStandardReviewRecord.getReviewReason());
		dataStandardReviewRecordDetail.setStatus(2);
		dataStandardReviewRecordDetail.setGmtModified(LocalDateTime.now());
		dataStandardReviewRecordDetail.setId(id);
		dataStandardReviewRecordService.updateById(dataStandardReviewRecordDetail);

		DataStandard dataStandard = dataStandardService.getById(dataStandardReviewRecordDetail.getDataStandardId());
		dataStandard.setStatus(2);
		dataStandardService.updateById(dataStandard);

		// TODO 通知发布者

	}

	@ApiOperation("查看拒绝理由")
	@GetMapping("{id}/reason")
	public DataStandardReviewRecord get(@PathVariable Integer id) {

		return dataStandardReviewRecordService.getById(id);
	}

	@ApiOperation("重新发布审核")
	@PutMapping("{id}/repub")
	public void rePub(@PathVariable Integer id, @Valid @RequestBody DataStandard dataStandard) {

		// 获取当前审核记录，审核人，比对
		DataStandardReviewRecord dataStandardReviewRecordDetail = dataStandardReviewRecordService.getById(id);
		Admin currentAdmin = ShiroUtils.currentAdmin();
		System.out.println(currentAdmin.getId());
		if (dataStandardReviewRecordDetail.getPubAdminId() != currentAdmin.getId()) {
			// 不是本人 ，报错
			throw new CustomException.ServiceException(
					CustomExceptionMessage.USERNAME_VIEWER_RECORD_CURRENT_ADMIN_NOT_RIGHT.getMessage());
		}
		
		dataStandardReviewRecordService.doRepubById(dataStandardReviewRecordDetail);
		//更新数据标准的字段内容
		DataStandard dbEentity = dataStandardService.getById(dataStandard.getId());
        CopyUtils.copyProperties(dataStandard, dbEentity);
        //设为待审核状态
        dbEentity.setStatus(0);
        Boolean result = dataStandardService.updateDataStandardForReviewer(dbEentity);
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation("数据标准待审核列表(关联数据标准的名称)")
	@GetMapping("page")
	public IPage<DataStandardReviewRecordVO> queryPage(DataStandardReviewRecordPageDTOQuery dataStandardReviewRecordPageDTOQuery) {

		Admin currentAdmin = ShiroUtils.currentAdmin(); 
		
//		IPage<DataStandardReviewRecordPageDTO> ipage=dataStandardReviewRecordService.selectDataStandardReviewRecordPageVo(dataStandardReviewRecordPageDTOQuery.page(), currentAdmin.getId(), currentAdmin.getId());
//		List<DataStandardReviewRecordPageDTO> dataStandardReviewRecordPageDTOList = ipage.getRecords();
//		for(DataStandardReviewRecordPageDTO dd:dataStandardReviewRecordPageDTOList) {
//			
//			List<DataStandardAppendFile> ll=dataStandardAppendFileService.getAppendFileByDataStandardId(dd.getDataStandardId());
//			dd.setDataStandardAppendFileList(ll);
//			System.out.println("=========="+ll.size());
//		}
		
		IPage<DataStandardReviewRecordVO> ipage=dataStandardReviewRecordService.queryDataStandardReviewPage(dataStandardReviewRecordPageDTOQuery.page(), currentAdmin.getId(), currentAdmin.getId());
		List<DataStandardReviewRecordVO> dataList = ipage.getRecords();
		for(DataStandardReviewRecordVO dd:dataList) {
			
			List<DataStandardAppendFile> ll=dataStandardAppendFileService.getAppendFileByDataStandardId(dd.getDataStandardId());
			dd.setDataStandardAppendFileList(ll);
			System.out.println("=========="+ll.size());
		}
		
		return ipage;
	}
	
	

}
