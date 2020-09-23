package dbServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

//为每一连接进来的客户端建立一个新的线程
public class TelnetServer {
	private ServerSocket server = null;
	 //_start
	 //创建一个可缓存的线程池，若需求长度超过线程池长度，线程池可灵活收回空闲线程
	private final ExecutorService executor = Executors.newFixedThreadPool(100);
	 //_end
	
	private int GIVEN_PORT=2223;//端口号
	
	public void telnetServer(String port) {
		GIVEN_PORT=port!=null?Integer.valueOf(port).intValue() : 0;
	}
	public void run() {
		try {
			//在指定端口建立连接
			server=new ServerSocket(GIVEN_PORT);
			System.out.println("Server running and listening on port : "+ GIVEN_PORT);
			while(true) {
				//开始监听
				Socket s=server.accept();
				//有客户端连接进入后，即启动一个线程，并将socket作为一个参数
				executor.execute(new ClientProcess(s));
			}
		}catch(IOException e) {
			System.out.println(Level.WARNING+ e.toString()+" Shutting down the server..");
		}finally {
			//关闭连接
			executor.shutdown();
		}
	}
	  /**
     * Checks if the server is running.
     *
     * @return
     */
    public boolean isRunning() {
        return !server.isClosed();
    }
    public void shutDown() throws IOException {
        if (server != null) {
            server.close();
        }
    }
}
