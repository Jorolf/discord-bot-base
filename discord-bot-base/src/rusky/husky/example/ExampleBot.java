package rusky.husky.example;

import java.util.Random;
import java.util.function.Consumer;

import rusky.husky.Bot;
import rusky.husky.CommandArgs;

public class ExampleBot extends Bot {

	public ExampleBot(String[] args) {
		super(args);
	}

	public static void main(String[] args) {
		ExampleBot bot = new ExampleBot(args);

		bot.addCommand("roll", new Roll());
	}
	
	private static class Roll implements Consumer<CommandArgs>{

		@Override
		public void accept(CommandArgs t) {
			int rng;
			if(t.args.length >= 1)
				rng = new Random().nextInt(Integer.parseInt(t.args[0]) + 1);
			else
				rng = new Random().nextInt(101);
			t.channel.sendMessage(t.sender.mention() + " rolls " + rng + (rng == 1 ? " point!" : " points!"));
		}
		
	}
}
