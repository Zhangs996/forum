package com.zhang.forum.dao.impl;

import com.zhang.forum.dao.AlphaDao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository

//在测试类加载bean中就是用这个impl，而不再使用AlphaDaoHibernateImpl，如果不加这个，applicationContext.getBean(AlphaDao.class)不知道会加载哪个实现类
@Primary
public class AlphaDaoMyBatisImpl implements AlphaDao {
    @Override
    public String select() {
        return "MyBatis";
    }
}
