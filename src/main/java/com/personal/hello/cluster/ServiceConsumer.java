/**
 * 
 */
package com.personal.hello.cluster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liuquan
 *
 */

public class ServiceConsumer {

	private static final Logger log = LoggerFactory.getLogger(ServiceConsumer.class);

	private List<String> serverList = new ArrayList<String>();

	/**
	 * @param zkServer
	 * @param path
	 */
	public void init(String zkServer,String path) {
		ZkClient zkClient = new ZkClient(zkServer);

		boolean serviceExists = zkClient.exists(path);
		if (serviceExists) {
			serverList = zkClient.getChildren(path);
		} else {
			throw new RuntimeException("service not exist!");
		}

		// 注册事件监听
		zkClient.subscribeChildChanges(path, new IZkChildListener() {
			// @Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				serverList = currentChilds;
			}
		});
	}

	// 消费服务
	public String consume() {
		// 通过负责均衡算法，得到一台服务器进行调用
		Random r = new Random();
		int index = 0;
		if (serverList.size() > 0) {
			index = r.nextInt(serverList.size());
			log.info("调用" + serverList.get(index) + "提供的服务");
			return serverList.get(index);
		}
		return null;
	}


	public static void main(String[] args) throws Exception {
		ServiceConsumer consumer = new ServiceConsumer();

		consumer.consume();

		Thread.sleep(1000 * 60 * 60 * 24);
	}

}
