package com.hkfs.fundamental.akka.utils;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.hkfs.fundamental.akka.SpringExtension;
import com.hkfs.fundamental.spring.utils.SpringContextUtils;

/**
 * Actor的工具类
 * Created by brucezee on 2017/3/7.
 */
public class ActorSystemUtils {
    /**
     * Actor系统的名称
     */
    public static final String ACTOR_SYSTEM_NAME = "AkkaSpringSystem";
    /**
     * Actor系统在spring容器中的bean的名称
     */
    public static final String ACTOR_SYSTEM_BEAN_NAME = "actorSystem";
    /**
     * Actor的Spring扩展的bean名称
     */
    public static final String SPRING_EXTENSION_BEAN_NAME = "springExtension";


    /**
     * 获取全局Actor System
     * @return
     */
    public static ActorSystem getActorSystem() {
        return SpringContextUtils.getBean(ACTOR_SYSTEM_BEAN_NAME, ActorSystem.class);
    }

    /**
     * 获取Spring的扩展
     * @return
     */
    public static SpringExtension getSpringExtension() {
        return SpringContextUtils.getBean(SPRING_EXTENSION_BEAN_NAME, SpringExtension.class);
    }

    /**
     * 根据spring的bean的名称获取配置
     * @param actorBeanName
     * @return
     */
    public static Props props(String actorBeanName) {
        return getSpringExtension().props(actorBeanName);
    }

    /**
     * 根据指定配置和actor的名称获取actor的引用（基于根节点）
     * @param props
     * @param actorName
     * @return
     */
    public static ActorRef actorOfSystem(Props props, String actorName) {
        return getActorSystem().actorOf(props, actorName);
    }

    /**
     * 根据指定配置获取actor的引用（基于根节点）
     * @param props
     * @return
     */
    public static ActorRef actorOfSystem(Props props) {
        return getActorSystem().actorOf(props);
    }

    /**
     * 根据指定bean的名称和actor的名称获取actor的引用（基于根节点）
     * @param actorBeanName
     * @param actorName
     * @return
     */
    public static ActorRef actorOfSystem(String actorBeanName, String actorName) {
        return actorOfSystem(props(actorBeanName), actorName);
    }

    /**
     * 根据指定bean的名称获取actor的引用（基于根节点）
     * @param actorBeanName
     * @return
     */
    public static ActorRef actorOfSystem(String actorBeanName) {
        return actorOfSystem(props(actorBeanName));
    }

    /**
     * 根据指定bean获取与bean名称相同的actor的引用（基于根节点）
     * @param actorOrBeanName
     * @return
     */
    public static ActorRef actorOfSystemWithBeanName(String actorOrBeanName) {
        return actorOfSystem(actorOrBeanName, actorOrBeanName);
    }



    /**
     * 根据指定配置和actor的名称获取actor的引用（基于当前节点）
     * @param context
     * @param props
     * @param actorName
     * @return
     */
    public static ActorRef actorOfContext(ActorContext context, Props props, String actorName) {
        return context.actorOf(props, actorName);
    }

    /**
     * 根据指定配置获取actor的引用（基于当前节点）
     * @param context
     * @param props
     * @return
     */
    public static ActorRef actorOfContext(ActorContext context, Props props) {
        return context.actorOf(props);
    }

    /**
     * 根据指定bean的名称和actor的名称获取actor的引用（基于当前节点）
     * @param context
     * @param actorBeanName
     * @param actorName
     * @return
     */
    public static ActorRef actorOfContext(ActorContext context, String actorBeanName, String actorName) {
        return actorOfContext(context, props(actorBeanName), actorName);
    }

    /**
     * 根据指定bean获取actor的引用（基于当前节点）
     * @param context
     * @param actorBeanName
     * @return
     */
    public static ActorRef actorOfContext(ActorContext context, String actorBeanName) {
        return actorOfContext(context, props(actorBeanName));
    }

    /**
     * 根据指定bean获取与bean名称相同的actor的引用（基于当前节点）
     * @param context
     * @param actorOrBeanName
     * @return
     */
    public static ActorRef actorOfContextWithBeanName(ActorContext context, String actorOrBeanName) {
        return actorOfContext(context, actorOrBeanName, actorOrBeanName);
    }
}
