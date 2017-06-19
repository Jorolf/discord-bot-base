package rusky.husky;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandListener implements IListener<MessageReceivedEvent> {
	private final Map<String, Consumer<CommandArgs>> commands = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private long lastCommand = System.currentTimeMillis();
	
	public void addCommand(String name, Consumer<CommandArgs> command){
		commands.put(name, command);
	}

	@Override
	public void handle(MessageReceivedEvent event) {
		if(System.currentTimeMillis() - lastCommand < 1500) return;
		if(event.getMessage().getContent().startsWith("!")){
			String[] parts = event.getMessage().getContent().substring(1).split(" ");
			if(commands.containsKey(parts[0])){
				commands.get(parts[0]).accept(new CommandArgs(parts.length == 1 ? new String[0] : Arrays.copyOfRange(parts, 1, parts.length), event.getAuthor(), event.getChannel()));
				lastCommand = System.currentTimeMillis();
			}
		}
	}

}
