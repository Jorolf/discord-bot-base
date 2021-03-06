package rusky.husky;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.Permissions;

public class CommandListener implements IListener<MessageReceivedEvent> {
	private final Map<String, Consumer<CommandArgs>> commands = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private Map<IChannel, Long> lastCommand = new HashMap<>();
	private Map<IChannel, Boolean> fastMessage = new HashMap<>();

	private final String prefix;

	public CommandListener(String prefix) {
		this.prefix = prefix;
	}

	public void addCommand(String name, Consumer<CommandArgs> command) {
		commands.put(name, command);
	}

	@Override
	public void handle(MessageReceivedEvent event) {
		if (!event.getChannel().getModifiedPermissions(event.getClient().getOurUser())
				.contains(Permissions.SEND_MESSAGES))
			return;
		if (System.currentTimeMillis() - lastCommand.getOrDefault(event.getChannel(), 0l) < 1500) {
			if (fastMessage.getOrDefault(event.getChannel(), true)){
				event.getChannel().sendMessage("2fast4me!");
				fastMessage.put(event.getChannel(), false);
			}
			return;
		}
		if (event.getMessage().getContent().startsWith(prefix)) {
			String[] parts = event.getMessage().getContent().substring(prefix.length()).split(" ");
			if (commands.containsKey(parts[0])) {
				try {
					commands.get(parts[0])
							.accept(new CommandArgs(
									parts.length == 1 ? new String[0] : Arrays.copyOfRange(parts, 1, parts.length),
									event.getAuthor(), event.getChannel()));
				} catch (Exception e) {
					e.printStackTrace();
					event.getClient().getApplicationOwner().getOrCreatePMChannel().sendMessage(event.getAuthor() + " managed to create an exception in " + (event.getGuild() != null ? event.getGuild().getName() + event.getChannel().mention() : "his PM channel!"));
					try(Writer w = new Writer() {
						private String s = "```\n";
						
						@Override
						public void write(char[] cbuf, int off, int len) throws IOException {
							s += String.copyValueOf(cbuf, off, len);
						}
						
						@Override
						public void flush() throws IOException {
							for(String message : (s + "\n```").split("(?<=\\G.{2000})"))
								event.getClient().getApplicationOwner().getOrCreatePMChannel().sendMessage(message);
						}
						
						@Override
						public void close() throws IOException {
							flush();
						}
					}){
						e.printStackTrace(new PrintWriter(w));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				lastCommand.put(event.getChannel(), System.currentTimeMillis());
				fastMessage.put(event.getChannel(), true);
			}
		}
	}

}
