/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.sktelecom.ssm.broker.external.mqttsclient;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.util.Timer;

import com.sktelecom.ssm.broker.external.mqttsclient.codec.MqttsDecoder;
import com.sktelecom.ssm.broker.external.mqttsclient.codec.MqttsEncoder;

/**
 * Creates a newly configured {@link ChannelPipeline} for a cliimport
 */
public class MqttsClientPipelineFactory implements ChannelPipelineFactory {
	private final Timer timer;
	private final ClientBootstrap bootstrap;
	private final String clientId;
	private static int WRITE_IDLE_SECOND;
	private static int READ_TIMEOUT_SECOND;

	public MqttsClientPipelineFactory(ClientBootstrap bootstrap, Timer timer, String clientId, int write, int read) {
		this.timer = timer;
		this.bootstrap = bootstrap;
		this.clientId = clientId;
		MqttsClientPipelineFactory.WRITE_IDLE_SECOND = write;
		MqttsClientPipelineFactory.READ_TIMEOUT_SECOND = read;
	}

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = pipeline();
		// Add the number codec first,
		pipeline.addLast("encoder", new MqttsEncoder());
		pipeline.addLast("decoder", new MqttsDecoder());

		// 30초 동안 보낸적이 없는지 체크~
		pipeline.addLast("idle", new IdleStateHandler(timer, 0, WRITE_IDLE_SECOND, 0));

		// 30초 지나서 LINK를 보냈는데 40초 동안 읽은게 없다면
		// LINK에 대한 응답이 없다는 이야기로 간주하고
		// 재접속 시도!!
		pipeline.addLast("read_timeout", new ReadTimeoutHandler(timer, READ_TIMEOUT_SECOND));

		// and then business logic.
		pipeline.addLast("handler", new MqttsClientHandler(bootstrap, clientId));
		return pipeline;
	}
}
