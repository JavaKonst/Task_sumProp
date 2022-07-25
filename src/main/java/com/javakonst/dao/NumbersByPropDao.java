package com.javakonst.dao;

import com.javakonst.entity.NumberByProp;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NumbersByPropDao extends CrudRepository<NumberByProp, Long> {

    NumberByProp findByNumberAndGender(String number, String gender);

    List<NumberByProp> findAll();
}
