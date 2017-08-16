package com.sinotopia.fundamental.pay.spdb.test;

import com.sinotopia.fundamental.pay.spdb.bean.WithDrawalsInfo;
import com.sinotopia.fundamental.pay.spdb.enums.BankCodeEnum;
import com.sinotopia.fundamental.pay.spdb.sender.SpdbHttpSender;
import org.apache.velocity.app.VelocityEngine;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.math.BigDecimal;
import java.util.Date;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext-spdb.xml"})
public class TestPlayMoneyUtil {

    @Autowired
    private SpdbHttpSender spdbHttpSender
            ;
    @Autowired
    private VelocityEngine velocityEngine;
    @Test
    public void testPlayMoney(){

        spdbHttpSender.setVelocityEngine(velocityEngine);

        WithDrawalsInfo withDrawalsInfo = new WithDrawalsInfo();

        withDrawalsInfo.setAmount(new BigDecimal("1"));
        withDrawalsInfo.setPacketId("1234568789");
        withDrawalsInfo.setTimestamp(new Date());
        withDrawalsInfo.setElecChequeNo("1234568789");
        withDrawalsInfo.setPayeeType(1);
        withDrawalsInfo.setPayeeAcctNo("6217566200010741995");
        withDrawalsInfo.setPayeeName("张安挺");
        withDrawalsInfo.setPayeeBankNo(BankCodeEnum.BOC.getSpdbBankCode());
        withDrawalsInfo.setTransStatus(0);

        spdbHttpSender.sendWithDrawEG(withDrawalsInfo);
        /*withDrawalsInfo.set*/
    }

    @Test
    public void test(){

        spdbHttpSender.setVelocityEngine(velocityEngine);

        WithDrawalsInfo withDrawalsInfo = new WithDrawalsInfo();

        withDrawalsInfo.setTransStatus(3);
        withDrawalsInfo.setPacketId("1601051200487102");
        withDrawalsInfo.setTimestamp(new Date());
        withDrawalsInfo.setElecChequeNo("160105120048710");
        withDrawalsInfo.setAcceptNo("IB01601050701267");
        spdbHttpSender.queryWithDrawEG(withDrawalsInfo);
        /*withDrawalsInfo.set*/
    }

}
