package com.hkfs.fundamental.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.hkfs.fundamental.akka.utils.ActorSystemUtils;

import java.io.Serializable;

/**
 * Akka服务基类
 * Created by brucezee on 2017/2/21.
 */
public class BaseAkkaService implements Serializable {
    public ActorSystem getActorSystem() {
        return ActorSystemUtils.getActorSystem();
    }

    public SpringExtension getSpringExtension() {
        return ActorSystemUtils.getSpringExtension();
    }

    public Props props(String actorBeanName) {
        return ActorSystemUtils.props(actorBeanName);
    }

    /**
     * 根据指定配置和actor的名称获取actor的引用（基于根节点）
     * @param props
     * @param actorName
     * @return
     */
    public ActorRef actorOfSystem(Props props, String actorName) {
        return ActorSystemUtils.actorOfSystem(props, actorName);
    }

    /**
     * 根据指定配置获取actor的引用（基于根节点）
     * @param props
     * @return
     */
    public ActorRef actorOfSystem(Props props) {
        return ActorSystemUtils.actorOfSystem(props);
    }

    /**
     * 根据指定bean的名称和actor的名称获取actor的引用（基于根节点）
     * @param actorBeanName
     * @param actorName
     * @return
     */
    public ActorRef actorOfSystem(String actorBeanName, String actorName) {
        return ActorSystemUtils.actorOfSystem(actorBeanName, actorName);
    }

    /**
     * 根据指定bean的名称获取actor的引用（基于根节点）
     * @param actorBeanName
     * @return
     */
    public ActorRef actorOfSystem(String actorBeanName) {
        return ActorSystemUtils.actorOfSystem(actorBeanName);
    }

    /**
     * 根据指定bean获取与bean名称相同的actor的引用（基于根节点）
     * @param actorOrBeanName
     * @return
     */
    public ActorRef actorOfSystemWithBeanName(String actorOrBeanName) {
        return ActorSystemUtils.actorOfSystemWithBeanName(actorOrBeanName);
    }
}
