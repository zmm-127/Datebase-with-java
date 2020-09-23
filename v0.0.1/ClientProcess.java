package dbServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientProcess implements Runnable{		//实现Runnable接口
	//设置socket连接对象
	private Socket socket;
	public ClientProcess(Socket socket) {
		this.socket = socket;
	}
	public void run() {
		try {
			//为每一个socket建立读写通道
			//创建BufferedReader对象用于读取数据
			final BufferedReader reader=
					new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//创建PrintWriter对象用于写数据
			final PrintWriter writer=new PrintWriter(socket.getOutputStream(),true);
			writer.println("**********Welcome to ZMDB***********");
			writer.println();
			//向连接访问者发送写数据
			writer.flush();
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

