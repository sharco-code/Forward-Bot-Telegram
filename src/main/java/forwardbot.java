import org.telegram.telegrambots.api.methods.ForwardMessage;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class forwardbot extends TelegramLongPollingBot {

    //forward variables
    private long forwardTo;
    private long forwardFrom;
    private boolean forwardStart;

    //variable to print actual chat
    private long actual_chatId;

    //forward function
    private ForwardMessage fwmsg(long to, long from, Integer msgid) {
        ForwardMessage fwmsg = new ForwardMessage();
        fwmsg.setChatId(to);
        fwmsg.setFromChatId(from);
        fwmsg.setMessageId(msgid);
        return fwmsg;
    }

    public void onUpdateReceived(Update update) {

        //this sentence prints the kind of chat that is
        if(actual_chatId == update.getMessage().getChatId()) {
            System.out.println(" [" + update.getMessage().getFrom().getUserName() + "]: " + update.getMessage().getText());
        } else {
            if(update.getMessage().getChat().isUserChat()) {
                System.out.println("#User: " + update.getMessage().getFrom().getUserName());
            } else if(update.getMessage().getChat().isGroupChat()) {
                System.out.println("#Group: " + update.getMessage().getChat().getTitle());
            } else if (update.getMessage().getChat().isChannelChat()) {
                System.out.println("#Channel: " + update.getMessage().getChat().getTitle());
            } else if(update.getMessage().getChat().isSuperGroupChat()) {
                System.out.println("#S-Group: " + update.getMessage().getChat().getTitle());
            } else {
                System.out.println("#WTF: " + update.getMessage().getChat().getTitle());
            }
            System.out.println(" [" + update.getMessage().getFrom().getUserName() + "]: " + update.getMessage().getText());
            actual_chatId = update.getMessage().getChatId();
        }

        //----------------------------------
        SendMessage output = new SendMessage().setChatId(update.getMessage().getChatId());
        output.setText(null);
        //----------------------------------

        //commands
        if("set forward here".equals(update.getMessage().getText())) {                      //set the chat to forward messages
            forwardTo = update.getMessage().getChatId();
            output.setText("Forward to added to this chat\n\n Group ID: " + forwardTo);
            System.out.println(output.getText());
        } else if ("set forward from".equals(update.getMessage().getText())) {              //set the chat from forward messages
            forwardFrom = update.getMessage().getChatId();
            output.setText("Forward from added to this chat\n\n Group ID" + forwardFrom);
            System.out.println(output.getText());
        } else if ("start forward".equals(update.getMessage().getText())) {                 //start forwarding messages
            if(forwardStart) {
                output.setText("Forward is already started");
                System.out.println(output.getText());
            } else {
                forwardStart = true;
                output.setText("Forward started");
                System.out.println(output.getText());
            }
        } else if ("stop forward".equals(update.getMessage().getText())) {                  //stop forwarding messages
            if(!forwardStart) {
                output.setText("Forward is already stopped");
                System.out.println(output.getText());
            } else {
                forwardStart = false;
                output.setText("Forward stopped");
                System.out.println(output.getText());
            }
        } else if ("help".equals(update.getMessage().getText())) {                          //says commands
            output.setText("List of commands:\nset forward here\nset forward from\nstart forward\nstop forward\nhelp");
            System.out.println(output.getText());
        }
        if(forwardStart) {
            if (forwardFrom == update.getMessage().getChatId()) {
                try {
                    execute(fwmsg(forwardTo, forwardFrom, update.getMessage().getMessageId()));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                    System.out.println("ERROR] in sendforward");
                }
            }
        }

        //this sentence sends a message... or not
        if(output.getText() != null) {
            try {
                execute(output);
            } catch (TelegramApiException e) {
                e.printStackTrace();
                System.out.println("[ERROR] in sendmessage");
            }
        }
    }

    public String getBotUsername() {
        return "BOT_USERNAME";
    }

    public String getBotToken() {
        return "API_TOKEN";
    }
}
