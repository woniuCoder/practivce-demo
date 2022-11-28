package com.imooc.socialecom.service.impl;

import com.imooc.socialecom.pojo.Test;
import com.imooc.socialecom.mapper.TestMapper;
import com.imooc.socialecom.service.TestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author socialecom
 * @since 2022-11-15
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements TestService {

}
