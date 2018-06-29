package com.le.jr.am.profit.test;

import com.le.jr.am.product.domain.Product;
import com.le.jr.am.product.interfaces.ProductInterfaces;
import com.le.jr.am.profit.service.PublisherProductAgreementService;
import com.le.jr.trade.publictools.data.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by dujianjun on 2017/3/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-config*.xml")
@ActiveProfiles("test")
public class testPublisherProductAgreementService {

    @Resource
    private PublisherProductAgreementService publisherProductAgreementService;

    @Resource
    private ProductInterfaces productInterfaces;

    @Test
    public void testprocessProductdts()throws  Exception{

        //this.publisherProductAgreementService.makeContract();
    }

    @Test
    public void testprocessProduct()throws  Exception{
        String productOid = "01140903";
        Message<Product> msg = this.productInterfaces.selectProductByOid(productOid);
       // this.publisherProductAgreementService.processProduct(msg.getData());
    }



}
