package com.zhang.forum.dao.impl;

import com.zhang.forum.dao.AlphaDao;
import org.springframework.stereotype.Repository;

//bean的名字
@Repository("alphaHibernate")
public class AlphaDaoHibernateImpl implements AlphaDao {
    @Override
    public String select() {
        return "Hibernate";
    }
}
