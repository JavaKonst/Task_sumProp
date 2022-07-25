package com.javakonst.service;

import com.javakonst.dao.NumbersByPropDao;
import com.javakonst.entity.NumberByProp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NumbersByPropService {

    @Autowired
    NumbersByPropDao numbersByPropDao;

    public NumberByProp getByNumberAndGender(String num, String gender){
        return numbersByPropDao.findByNumberAndGender(num, gender);
    }

    public void saveNumber(NumberByProp p){
        numbersByPropDao.save(p);
    }

    public List<NumberByProp> getAllNumbers(){
        return numbersByPropDao.findAll();
    }
}
