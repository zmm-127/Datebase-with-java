package dbServer;

import java.io.*;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

public class ClientManage implements Runnable{
	//创建线程
	private Thread t;
	//用于保存连接的客户端
	private Vector vSocket;
	//用于保存客户端发送的消息
	private Vector vMessage;
	
	ClientManage(){//构造函数
		System.out.println("Creating ");
		vSocket=new Vector();
		vMessage=new Vector();
	}
	public void addSocket(Object o) {//添加客户端socket连接
		vSocket.addElement(o);
	}
	public synchronized void addMessage(String str) {//添加客户端发送的消息
		vMessage.addElement(str);
	}
	public synchronized void processMessage() throws Exception{//将vMessage中的消息发送给连接的每一个客户端
		if(0==vMessage.size())
			return;
		for(int i=0;i<vMessage.size();i++) {//把每一个vMessage消息发送给每一个连接的vSocket
			for(int j=0;j<vSocket.size();j++) {
				Socket s=(Socket) vSocket.get(j);
				PrintWriter writer=new PrintWriter(s.getOutputStream(),true);//获取socket发送的消息
				writer.println(vMessage.get(i));
				writer.flush();
			}
		}
		for(int j=0;j<vSocket.size();j++) {//输出连接的客户端的提示信息
			Socket s=(Socket) vSocket.get(j);
			PrintWriter writer=new PrintWriter(s.getOutputStream(),true);
			writer.print("ZMDB>");
			writer.flush();
		}
		vMessage.removeAllElements();
	}
	public Vector getvSocket() {//获取连接的客户端
		return vSocket;
	}
	public void setvSocket(Vector vSocket) {
		this.vSocket=vSocket;
	}
	public Vector getvMessage() {//获取发送的消息
		return vMessage;
	}
	public void setvMessage(Vector vMessage) {
		this.vMessage=vMessage;
	}
	public void run(){
		try {
			while(true) {
				for(int i=vSocket.size()-1;i>=0;i--) {//对vSocket进行遍历
					Socket s=(Socket) vSocket.get(i);//获取保存的每一个scoket
					if(s.isClosed())
						vSocket.remove(i);
				}
				this.processMessage();
				Thread.sleep(200);//每隔200ms，就对客户端连接进行检测，然后将vMessage中的信息进行群发
			}
		}catch(Exception e) {
			
		}
	}
	public void start() {
		System.out.println("客户端管理线程开始");
		if(t==null) {
			t=new Thread(this);
			t.start();
		}
	}
}
