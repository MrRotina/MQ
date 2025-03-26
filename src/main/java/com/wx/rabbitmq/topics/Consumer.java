package com.wx.rabbitmq.topics;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) {

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
            connection = connectionFactory.newConnection("消费者");

//    3.通过连接获取通道Channel
            channel = connection.createChannel();

//  指定一个消息队列获取消息
            String queueName = "我的消息队列1";
            //这里的true指的是是否自动确认
            channel.basicConsume(queueName, true, new DeliverCallback() {
                        @Override
                        public void handle(String s, Delivery delivery) throws IOException {
                            System.out.println("收到的消息是：" + new String(delivery.getBody(), "UTF-8"));
                        }
                    }, new CancelCallback() {
                        @Override
                        public void handle(String s) throws IOException {
                            System.out.println("接收消息失败");
                        }
                    });
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
