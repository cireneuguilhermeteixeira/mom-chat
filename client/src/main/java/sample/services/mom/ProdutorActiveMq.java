package sample.services.mom;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import sample.models.Mensagem;

import java.io.Serializable;

public class ProdutorActiveMq {

	/*
	 * URL do servidor JMS. DEFAULT_BROKER_URL indica que o servidor está em localhost
	 *
	 */
	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;



	public static void send(Mensagem mensagemRecebidaPeloApp) throws JMSException {

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
		Destination destination = session.createQueue(mensagemRecebidaPeloApp.getCelularReceber());

		/*
		 * Criando Produtor
		 */
		MessageProducer producer = session.createProducer(destination);
		Message message = session.createTextMessage(mensagemRecebidaPeloApp.convertToString());

		/*
		 * Enviando Mensagem
		 */

		producer.send(message);

		System.out.println("Messagem: '" +mensagemRecebidaPeloApp.getConteudo() + ", Enviada para a Fila");

		producer.close();
		session.close();
		connection.close();
	}
}
