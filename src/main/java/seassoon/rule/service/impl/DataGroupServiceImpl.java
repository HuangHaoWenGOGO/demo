package seassoon.rule.service.impl;

import seassoon.rule.dto.query.DataGroupQuery;
import seassoon.rule.entity.DataGroup;
import seassoon.rule.mapper.DataGroupMapper;
import seassoon.rule.service.DataGroupService;
import seassoon.rule.vo.DataGroupVO;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangqianfeng
 * @since 2020-07-26
 */
@Service
@AllArgsConstructor
public class DataGroupServiceImpl extends ServiceImpl<DataGroupMapper, DataGroup> implements DataGroupService {
	
	private DataGroupMapper dataGroupMapper;
	
    @Override
    public Page<DataGroup> query(DataGroupQuery dataGroupQuery) {
        QueryWrapper<DataGroup> queryWrapper = new QueryWrapper<>();


        if (StringUtils.isNotBlank(dataGroupQuery.getKeyword())) {
            queryWrapper.like("name", dataGroupQuery.getKeyword());
        }
        return this.page(dataGroupQuery.page(), queryWrapper);
    }
    
    
    public IPage<DataGroupVO> queryDataGroupPage(Page<DataGroupVO> page,String keyword){
    	
    	return dataGroupMapper.queryDataGroupPage(page,keyword);
    	
    }

}
