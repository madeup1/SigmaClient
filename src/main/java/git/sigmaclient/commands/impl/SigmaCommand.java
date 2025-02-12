package git.sigmaclient.commands.impl;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.commands.Command;
import git.sigmaclient.managers.CommandManager;

public class SigmaCommand extends Command {
    public SigmaCommand()
    {
        super("sigma");
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length > 2)
        {
            SigmaClient.sendMessageWithPrefix(".sigma <help/check/disconnect>");
            return;
        }

        if(args.length > 1 && args[1].equals("help")) {
            CommandManager.printHelp();
        }
    }

    @Override
    public String getDescription() {
        return ".sigma <help/update/disconnect>";
    }
}