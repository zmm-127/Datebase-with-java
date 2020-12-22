package dbServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

//Ϊÿһ���ӽ����Ŀͻ��˽���һ���µ��߳�
public class TelnetServer {
	private ServerSocket server = null;
	 //_start
	 //����һ���ɻ�����̳߳أ������󳤶ȳ����̳߳س��ȣ��̳߳ؿ�����ջؿ����߳�
	private final ExecutorService executor = Executors.newFixedThreadPool(100);
	 //_end
	
	private int GIVEN_PORT=2123;//�˿ں�
	private ClientManage cm;//����ClientManage�Ķ���
	private ServerManage sm;//����ServerManage�Ķ���
	public void telnetServer(String port) {//�Զ˿ڽ����ж��Ƿ�Ϊ��
		GIVEN_PORT=port!=null?Integer.valueOf(port).intValue() : 0;
	}
	public void run() {
		try {
			//��ָ���˿ڽ�������
			server=new ServerSocket(GIVEN_PORT);
			System.out.println("Server running and listening on port : "+ GIVEN_PORT);
			cm = new ClientManage();//�����ͻ��˵Ĺ���
			cm.start();
			sm=new ServerManage(cm);//�����������˵Ĺ���
			sm.start();
			//����ͻ��˹���
			executor.execute(cm);
			//����������˹���
			executor.execute(sm);
			while(true) {
				//��ʼ����
				Socket s=server.accept();
				cm.addSocket(s);
				//�пͻ������ӽ���󣬼�����һ���̣߳�����socket��Ϊһ������
				executor.execute(new ClientProcess(s,cm));
			}
		}catch(IOException e) {
			System.out.println(Level.WARNING+ e.toString()+" Shutting down the server..");
		}finally {
			//�ر�����
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
