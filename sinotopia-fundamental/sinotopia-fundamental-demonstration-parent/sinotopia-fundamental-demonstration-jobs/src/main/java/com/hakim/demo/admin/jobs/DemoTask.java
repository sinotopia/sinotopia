package com.hakim.demo.admin.jobs;

import com.sinotopia.fundamental.common.utils.TimeUtils;
import org.springframework.stereotype.Service;

/**
 * 演示任务
 * Created by brucezee on 2017/3/14.
 */
@Service("demoTask")
public class DemoTask {

    public void execute() {
        System.out.println(TimeUtils.getFormattedTime() + "Hello, I'm alive!");
    }

}
