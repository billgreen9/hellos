/**
 * 
 */
package com.personal.hello.cluster;

import java.net.InetAddress;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 集群服务注册中心
 * 
 * @author liuquan
 *
 */
public class Registry {
	
	private static final Logger log = LoggerFactory.getLogger(Registry.class);

	// 向zookeeper注册服务
	public static void add(String zkServer,String parent,String name){
		ZkClient zkClient = new ZkClient(zkServer);
		boolean rootExists = zkClient.exists(parent);
		if (!rootExists) {
			zkClient.createPersistent(parent);
		}
		// 判断是否存在，不存在则创建服务节点
		boolean serviceExists = zkClient.exists(parent);
		String path = parent + "/" + name;
		if (!serviceExists) {
			zkClient.createPersistent(path);
		}
		// 創建當前服務器節點
		zkClient.createEphemeral(path);
		log.info("提供的服务节点名称为：" + path);
	}


	public static void main(String[] args) throws Exception {
		Registry service = new Registry();
		//Registry.add();
	}

}
