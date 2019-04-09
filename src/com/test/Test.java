package com.test;

import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;


public class Test extends BaseBean{

	public static void main(String[] args) throws Exception {
		System.out.println(Util.TokenizerStringNew(TimeUtil.getCurrentTimeString(), " ")[1]);
	}
	
}