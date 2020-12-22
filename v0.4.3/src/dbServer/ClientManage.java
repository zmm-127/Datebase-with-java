package dbServer;

import java.io.*;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

public class ClientManage implements Runnable{
	//�����߳�
	private Thread t;
	//���ڱ������ӵĿͻ���
	private Vector vSocket;
	//���ڱ���ͻ��˷��͵���Ϣ
	private Vector vMessage;
	
	ClientManage(){//���캯��
		System.out.println("Creating ");
		vSocket=new Vector();
		vMessage=new Vector();
	}
	public void addSocket(Object o) {//��ӿͻ���socket����
		vSocket.addElement(o);
	}
	public synchronized void addMessage(String str) {//��ӿͻ��˷��͵���Ϣ
		vMessage.addElement(str);
	}
	public synchronized void processMessage() throws Exception{//��vMessage�е���Ϣ���͸����ӵ�ÿһ���ͻ���
		if(0==vMessage.size())
			return;
		for(int i=0;i<vMessage.size();i++) {//��ÿһ��vMessage��Ϣ���͸�ÿһ�����ӵ�vSocket
			for(int j=0;j<vSocket.size();j++) {
				Socket s=(Socket) vSocket.get(j);
				PrintWriter writer=new PrintWriter(s.getOutputStream(),true);//��ȡsocket���͵���Ϣ
				writer.println(vMessage.get(i));
				writer.flush();
			}
		}
		for(int j=0;j<vSocket.size();j++) {//������ӵĿͻ��˵���ʾ��Ϣ
			Socket s=(Socket) vSocket.get(j);
			PrintWriter writer=new PrintWriter(s.getOutputStream(),true);
			writer.print("ZMDB>");
			writer.flush();
		}
		vMessage.removeAllElements();
	}
	public Vector getvSocket() {//��ȡ���ӵĿͻ���
		return vSocket;
	}
	public void setvSocket(Vector vSocket) {
		this.vSocket=vSocket;
	}
	public Vector getvMessage() {//��ȡ���͵���Ϣ
		return vMessage;
	}
	public void setvMessage(Vector vMessage) {
		this.vMessage=vMessage;
	}
	public void run(){
		try {
			while(true) {
				for(int i=vSocket.size()-1;i>=0;i--) {//��vSocket���б���
					Socket s=(Socket) vSocket.get(i);//��ȡ�����ÿһ��scoket
					if(s.isClosed())
						vSocket.remove(i);
				}
				this.processMessage();
				Thread.sleep(200);//ÿ��200ms���ͶԿͻ������ӽ��м�⣬Ȼ��vMessage�е���Ϣ����Ⱥ��
			}
		}catch(Exception e) {
			
		}
	}
	public void start() {
		System.out.println("�ͻ��˹����߳̿�ʼ");
		if(t==null) {
			t=new Thread(this);
			t.start();
		}
	}
}
