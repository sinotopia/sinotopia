package com.sinotopia.fundamental.akka;

import akka.actor.*;
import com.sinotopia.fundamental.akka.utils.ActorSystemUtils;

/**
 * UntypedActor基类
 * Created by brucezee on 2017/2/23.
 */
public abstract class BaseUntypedActor extends UntypedActor {
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
     * 根据指定配置和actor的名称获取actor的引用（基于当前节点）
     * @param props
     * @param actorName
     * @return
     */
    public ActorRef actorOfContext(Props props, String actorName) {
        return ActorSystemUtils.actorOfContext(getContext(), props, actorName);
    }

    /**
     * 根据指定配置获取actor的引用（基于当前节点）
     * @param props
     * @return
     */
    public ActorRef actorOfContext(Props props) {
        return ActorSystemUtils.actorOfContext(getContext(), props);
    }

    /**
     * 根据指定bean的名称和actor的名称获取actor的引用（基于当前节点）
     * @param actorBeanName
     * @param actorName
     * @return
     */
    public ActorRef actorOfContext(String actorBeanName, String actorName) {
        return ActorSystemUtils.actorOfContext(getContext(), actorBeanName, actorName);
    }

    /**
     * 根据指定bean获取actor的引用（基于当前节点）
     * @param actorBeanName
     * @return
     */
    public ActorRef actorOfContext(String actorBeanName) {
        return ActorSystemUtils.actorOfContext(getContext(), actorBeanName);
    }

    /**
     * 根据指定bean获取与bean名称相同的actor的引用（基于当前节点）
     * @param actorOrBeanName
     * @return
     */
    public ActorRef actorOfContextWithBeanName(String actorOrBeanName) {
        return ActorSystemUtils.actorOfContextWithBeanName(getContext(), actorOrBeanName);
    }

}
