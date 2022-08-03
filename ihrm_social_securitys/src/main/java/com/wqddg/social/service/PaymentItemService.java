package com.wqddg.social.service;


import com.sun.xml.internal.bind.v2.model.core.ID;
import com.wqddg.domain.social_security.CityPaymentItem;
import com.wqddg.social.dao.CityPaymentItemDao;
import com.wqddg.social.dao.PaymentItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentItemService {

    @Autowired
    private PaymentItemDao itemDao;
    @Autowired
    private CityPaymentItemDao cityPaymentItemDao;

    /**
     * 根据城市id 获取缴费项目
     * @param id
     * @return
     */
    public List<CityPaymentItem> findAllByCityId(String id) {
        return cityPaymentItemDao.findAllByCityId(id);
    }
}
