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
			writer.println(socket.toString());
			//向连接访问者发送写数据
			writer.flush();//清空缓冲区数据
			//StringBuilder 类的对象能够被多次的修改，并且不产生新的未使用对象
			//创建StringBuilder 类的对象
			StringBuilder strbuilder=new StringBuilder();
			while(true) {//客户端一直保留登录状态
				if(reader==null||writer==null) {//若读写数据均为空，则退出程序
					return;
				}
				strbuilder.delete(0, strbuilder.length());//删除从0开始到之前字符串结尾位置之间的字符串
				writer.print("ZMDB>");
				writer.flush();
				String line=reader.readLine();//读取客户端的数据
				if(line==null)//line为空值，表明客户端发生异常
					return;
				strbuilder.append(line);//将输入的客户端数据连接起来
				if("logout".equals(strbuilder.toString().toLowerCase())) {//在客户端输入logout，则退出
					writer.println("Thanks for using ZMDB!Byebye!");
					writer.flush();
					return;
				}else {
					writer.println(strbuilder.toString());//输出一个与构建器或缓冲器内容相同的字符串（即客户端输入的内容）
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

