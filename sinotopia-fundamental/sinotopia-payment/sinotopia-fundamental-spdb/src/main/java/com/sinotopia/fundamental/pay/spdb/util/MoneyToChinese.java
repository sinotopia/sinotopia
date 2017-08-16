package com.sinotopia.fundamental.pay.spdb.util;

import java.text.DecimalFormat;

public class MoneyToChinese { 
	
    /**
     * 数字金额大写转换
     */
    public static String digitUppercase(int money){
    	
    	DecimalFormat df2 = new DecimalFormat("###");
    	
    	if(money >= 10000){
    		
    		return  df2.format(money/10000)+"万元";
    		
    	}else{
    		
    		return String.valueOf(money)+"元";
    		
    	}
    }
    
}
