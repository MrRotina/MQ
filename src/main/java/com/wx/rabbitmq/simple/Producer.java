package com.wx.rabbitmq.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 */
public class Producer {

    public static void main(String[] args) {
        //所有的中间件都是基于tcp/ip协议，rabbitmq基于amqp协议

        //ip port

//    1.创建连接工程
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("10.211.55.6" );
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("/");

        Connection connection = null;
        Channel channel = null;

        try {
            //    2.创建连接Connection
            connection = connectionFactory.newConnection("生产者");

//    3.通过连接获取通道Channel
            channel = connection.createChannel();
//    4.通过创建交换机，声明队列，绑定关系，路由key，发送消息和接收消息
            String queueName = "我的消息队列1";
            /**
             * @param1 队列名称
             * @param2 是否持久化,默认false
             * @param3 排他性,是否独占独立
             * @param4 是否自动删除，最后一个消费者消费完毕后，是否删除
             * @param5 携带附属参数
             */
            channel.queueDeclare(queueName, false, false, false, null);
//    5.准备消息内容
            String message = "hello world";
//    6.发送消息给队列
            channel.basicPublish("", queueName, null, message.getBytes());
            System.out.println("消息发送成功");
//    7.关闭通道和连接
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } finally {
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null && connection.isOpen()) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
