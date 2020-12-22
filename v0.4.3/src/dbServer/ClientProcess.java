package dbServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientProcess implements Runnable{		//ʵ��Runnable�ӿ�
	//�½�socket���Ӷ���
	private Socket socket;
	private ClientManage cm;//�½�ClientManage����
	public ClientProcess(Socket socket,ClientManage cm) {//��ʼ������
		this.socket = socket;
		this.cm=cm;
	}
	public void run() {
		try {
			//Ϊÿһ��socket������дͨ��
			//����BufferedReader�������ڶ�ȡ����
			final BufferedReader reader=
					new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//����PrintWriter��������д����
			final PrintWriter writer=new PrintWriter(socket.getOutputStream(),true);
			writer.println("**********Welcome to ZMDB***********");
			writer.println();
			writer.println(socket.toString());
			//�����ӷ����߷���д����
			writer.flush();//��ջ���������
			//StringBuilder ��Ķ����ܹ�����ε��޸ģ����Ҳ������µ�δʹ�ö���
			//����StringBuilder ��Ķ���
			StringBuilder strbuilder=new StringBuilder();
			while(true) {//�ͻ���һֱ������¼״̬����ȡ�ͻ������ݣ�ֱ���ͻ��˹رջ�����logout
				if(reader==null||writer==null) {//����д���ݾ�Ϊ�����˳�����
					return;
				}
				strbuilder.delete(0, strbuilder.length());//ɾ����0��ʼ��֮ǰ�ַ�����βλ��֮����ַ���
				writer.print("ZMDB>");
				writer.flush();
				String line=reader.readLine();//��ȡ�ͻ��˵�����
				if(line==null)//lineΪ��ֵ�������ͻ��˷����쳣
					return;
				cm.addMessage(line);
				strbuilder.append(line);//������Ŀͻ���������������
				if("logout".equals(strbuilder.toString().toLowerCase())) {//�ڿͻ�������logout�����˳�
					writer.println("Thanks for using ZMDB!Byebye!");
					writer.flush();
					return;
				}else {
					writer.println(strbuilder.toString());//���һ���빹�����򻺳���������ͬ���ַ��������ͻ�����������ݣ�
					writer.flush();
				}
			}
		}catch(IOException e) {
			System.out.println(e.toString());
		}finally {
			try {
				socket.close();
			}catch(IOException e) {
				
			}
		}
	}
}

