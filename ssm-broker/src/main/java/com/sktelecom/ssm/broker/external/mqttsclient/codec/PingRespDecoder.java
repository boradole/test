/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sktelecom.ssm.broker.external.mqttsclient.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

import com.sktelecom.ssm.broker.external.mqttsclient.messages.PingRespMessage;

public class PingRespDecoder {

	public Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer, PingRespMessage msg) {
		PingRespMessage message = msg;

		return message;
	}
}
