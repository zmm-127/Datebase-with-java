package dbServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerManage implements Runnable{
	//�����̶߳���
	private Thread t;
	//�����ͻ��˹������
	private ClientManage cm;
	
	//��ʼ���ͻ��˹������
	ServerManage(ClientManage cm){
		this.cm = cm;
	}
		
	public void run() {
		Scanner sn = new Scanner(System.in);//�Ӽ��̻�ȡ��������
		String line = new String();//�����ַ��������

		//��ѭ�������ڿ���̨���������������Ӧ���������ݲ�ͬ������в�ͬ����
		while(true) {
			if(sn.hasNextLine()) {// �ж��Ƿ�������
				line = sn.nextLine();
				if(line.equals("shutdown")) {System.exit(0);}
				else if(line.equals("showclientnum")) {//���ؿͻ����û�����
					System.out.println(cm.getvSocket().size());
				}
				if(line.equals("showallclientinfo")) {//��ʾ���пͻ��˵�����
					Vector temp = cm.getvSocket();
					for(int i = temp.size()-1;i>=0;i--) {
						Socket so = (Socket)temp.elementAt(i);
						System.out.println(so);
					}
				}
			}
		}
	}
	//����һ�����߳�
	public void start() {
		System.out.println("DB�����������߳̿���");
		if(t == null) {
			t = new Thread(this);
			t.start();
		}
	}
}

