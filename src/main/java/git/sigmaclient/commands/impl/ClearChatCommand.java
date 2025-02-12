package git.sigmaclient.commands.impl;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.commands.Command;

public class ClearChatCommand extends Command {
    public ClearChatCommand()
    {
        super("clear");
    }
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            SigmaClient.sendMessageWithPrefix("Invalid command!");
            return;
        }

        for(int i = 0; i < Integer.parseInt(args[1]); i++) {
            SigmaClient.sendMessage("");
        }
    }

    @Override
    public String getDescription() {
        return ".clear <lines>";
    }
}