package seassoon.rule.service;

import seassoon.rule.entity.DataStandardAppendFile;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 数据标准关联的文件表 服务类
 * </p>
 *
 * @author Wayne
 * @since 2020-08-03
 */
public interface DataStandardAppendFileService extends IService<DataStandardAppendFile> {
	
	/**
	 * 根据数据组id找数据标准集
	 * @param dataGroupId
	 * @return
	 */
	public List<DataStandardAppendFile> getAppendFileByDataStandardId(Integer dataStandardId);

}
