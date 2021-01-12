package seassoon.rule.service.impl;

import seassoon.rule.entity.DataStandardAppendFile;
import seassoon.rule.mapper.DataStandardAppendFileMapper;
import seassoon.rule.service.DataStandardAppendFileService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 数据标准关联的文件表 服务实现类
 * </p>
 *
 * @author Wayne
 * @since 2020-08-03
 */
@Service
public class DataStandardAppendFileServiceImpl extends ServiceImpl<DataStandardAppendFileMapper, DataStandardAppendFile> implements DataStandardAppendFileService {

	/**
	 * 根据数据组id找数据标准集
	 * @param dataGroupId
	 * @return
	 */
	@Override
	public List<DataStandardAppendFile> getAppendFileByDataStandardId(Integer dataStandardId){
		QueryWrapper<DataStandardAppendFile> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("data_standard_id", dataStandardId);
		return this.list(queryWrapper);
	}
	
}
