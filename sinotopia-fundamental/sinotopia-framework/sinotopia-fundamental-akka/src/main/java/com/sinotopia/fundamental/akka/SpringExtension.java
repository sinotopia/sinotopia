package com.hkfs.fundamental.akka;

import akka.actor.Extension;
import akka.actor.Props;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Akka的spring扩展
 * Created by brucezee on 2017/2/21.
 */
public class SpringExtension implements Extension, ApplicationContextAware {
    private static volatile SpringExtension instance;
    private volatile ApplicationContext applicationContext;

    public SpringExtension() {
        instance = this;
    }

    public static SpringExtension getInstance() {
        if (instance == null) {
            synchronized (SpringExtension.class) {
                if (instance == null) {
                    instance = new SpringExtension();
                }
            }
        }
        return instance;
    }

    /**
     * Create a Props for the specified actorBeanName using the
     * SpringActorProducer class.
     *
     * @param actorBeanName The name of the actor bean to create Props for
     * @return a Props that will create the named actor bean using Spring
     */
    public Props props(String actorBeanName, Object ... args) {
        return Props.create(SpringActorProducer.class, applicationContext, actorBeanName, args);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
