package rusky.husky;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class CommandArgs {
	public String[] args;
	public IUser sender;
	public IChannel channel;
	
	public CommandArgs(String[] args, IUser sender, IChannel channel) {
		this.args = args;
		this.sender = sender;
		this.channel = channel;
	}
}
