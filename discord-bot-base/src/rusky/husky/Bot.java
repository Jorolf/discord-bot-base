package rusky.husky;

import java.util.function.Consumer;

import javax.swing.JOptionPane;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;

public class Bot {
	private CommandListener listener;
	
	public Bot(String token){
		listener = new CommandListener();

		IDiscordClient client;
		if(token != null)
			client = new ClientBuilder()
			.withToken(token)
			.build();
		else
			client = new ClientBuilder()
			.withToken(JOptionPane.showInputDialog("Enter the bot token\n(Protip: you can set the token as an argument)"))
			.build();
		
		client.getDispatcher().registerListener(listener);
		client.login();
	}
	
	public void addCommand(String name, Consumer<CommandArgs> command){
		listener.addCommand(name, command);
	}
}
