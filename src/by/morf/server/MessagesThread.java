package by.morf.server;

public class MessagesThread implements Runnable {

    public void run() {
        while(true){
            try{
                Object message = Server.messages.take();
                for(ListenerThread client : Server.clientList) {
                    client.sendData(message);
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
