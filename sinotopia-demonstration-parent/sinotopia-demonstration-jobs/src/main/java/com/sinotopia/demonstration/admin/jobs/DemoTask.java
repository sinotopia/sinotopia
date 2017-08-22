package com.sinotopia.demonstration.admin.jobs;

import com.sinotopia.fundamental.common.utils.TimeUtils;
import org.springframework.stereotype.Service;

/**
 * 演示任务
 */
@Service("demoTask")
public class DemoTask {

    public void execute() {
        System.out.println(TimeUtils.getFormattedTime() + "Hello, I'm alive!");
    }

}
