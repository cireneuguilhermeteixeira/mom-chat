package sample.services.mom;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;


public class ConsumidorActiveMq {

	/*
	 * URL do servidor JMS. DEFAULT_BROKER_URL indica que o servidor está em localhost
	 *
	 */
	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;


	public static Message receive(String nomeFila) throws JMSException {
		/*
		 * Estabelecendo conexão com o Servidor JMS
		 */
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		Connection connection = connectionFactory.createConnection();
		connection.start();

		/*
		 * Criando Session
		 */
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		/*
		 * Criando Queue
		 */
		Destination destination = session.createQueue(nomeFila);

		/*
		 * Criando Consumidor
		 */
		MessageConsumer consumer = session.createConsumer(destination);

		/*
		 * Recebendo Mensagem
		 */
		Message message = consumer.receive();

		consumer.close();
        session.close();
		connection.close();

		return message;

	}
}
