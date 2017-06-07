package by.morf.server;

public class MessagesThread implements Runnable {

    public void run() {
        while(true){
            try{
                MessageObj message = (MessageObj) Server.messages.take();
                for(ListenerThread client : Server.clientList) {
                    if (client.isAlive()) {
                        client.sendData(message);
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
