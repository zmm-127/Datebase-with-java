package dbServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public class ServerManage implements Runnable{
	//创建线程对象
	private Thread t;
	//创建客户端管理对象
	private ClientManage cm;
	
	//初始化客户端管理对象
	ServerManage(ClientManage cm){
		this.cm = cm;
	}
		
	public void run() {
		Scanner sn = new Scanner(System.in);//从键盘获取输入数据
		String line = new String();//定义字符串类对象

		//死循环监听在控制台听命令如果听到相应的命令便根据不同命令进行不同操作
		while(true) {
			if(sn.hasNextLine()) {// 判断是否还有输入
				line = sn.nextLine();
				if(line.equals("shutdown")) {}
				else if(line.equals("showclientnum")) {//返回客户端用户个数
					System.out.println(cm.getvSocket().size());
				}
				if(line.equals("showallclientinfo")) {//显示所有客户端的输入
					Vector temp = cm.getvSocket();
					for(int i = temp.size()-1;i>=0;i--) {
						Socket so = (Socket)temp.elementAt(i);
						System.out.println(so);
					}
				}
			}
		}
	}
	//创建一个新线程
	public void start() {
		System.out.println("DB服务器管理线程开启");
		if(t == null) {
			t = new Thread(this);
			t.start();
		}
	}
}

