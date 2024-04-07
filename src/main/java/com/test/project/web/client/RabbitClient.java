package com.test.project.web.client;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.test.project.config.AppConfig;
import com.test.project.dto.ClientDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import com.rabbitmq.client.Channel;
import java.util.function.Consumer;

@Service
@EnableRabbit
@RequiredArgsConstructor
@Slf4j
public class RabbitClient {

	@Value("${spring.rabbitmq.host}")
	private String rabbitmqHost;

	@Value("${spring.rabbitmq.username}")
	private String rabbitmqUser;

	@Value("${spring.rabbitmq.password}")
	private String rabbitmqPassword;

	@Value("${spring.rabbitmq.port}")
	private int rabbitmqPort;

	private final AppConfig appConfig;

	public Channel initChannel() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(rabbitmqHost);
		factory.setUsername(rabbitmqUser);
		factory.setPassword(rabbitmqPassword);
		factory.setPort(rabbitmqPort);
		Channel channel = null;
		try {
			Connection rmqConnection = factory.newConnection();
			channel = rmqConnection.createChannel();
		} catch (Exception e) {
			log.error("Ошибка создания подключения либо канала RabbitMQ {} ",  e.getMessage());
		}
		return channel;
	}

	public void initClientExchange(Channel channel) {
		try {
			channel.exchangeDeclare(appConfig.getClientExchange(),  BuiltinExchangeType.DIRECT, true);
		} catch (IOException e) {
			log.error("Ошибка создания иксченджа  {} ",  e.getMessage());
		}
	}


	public void initMessageListener(Channel channel, String queueName, String exchange, String bindKey, Consumer<ClientDto> consumer) {


		DeliverCallback deliverCallback = (str, delivery) -> {

			ClientDto rmqMessage = null;
			try {
				rmqMessage =  SerializationUtils.deserialize(delivery.getBody());
				log.info("RabbitMQ сообщение: {}", rmqMessage.toString());
			} catch (Exception e) {
				log.error("Ошибка  чтения сообщения RabbitMQ: {}", e.getMessage());
			}

			consumer.accept(rmqMessage);
		};
		try {
			channel.queueDeclare(queueName, true, false, false, null);
			channel.queueBind(queueName, exchange, bindKey);
			channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {	});

		} catch (IOException e) {
			log.error("Ошибка  чтения сообщения RabbitMQ: {}", e.getMessage());
		}
	}


	public  void publish(Channel channel,  String exchange, String routingKey, byte[] clientMessage) {
		try {
			channel.basicPublish(exchange, routingKey, null, clientMessage);
		} catch (IOException e) {
			log.error("Ошибка публикации:{}", e.getMessage());
		}
	}
}