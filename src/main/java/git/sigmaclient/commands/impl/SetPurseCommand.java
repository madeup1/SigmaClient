package git.sigmaclient.commands.impl;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.commands.Command;

public class SetPurseCommand extends Command {
    public SetPurseCommand()
    {
        super("purse", "setpurse","setcoins");
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2)
        {
            SigmaClient.sendMessageWithPrefix("Invalid command!");
            return;
        }

        double value = Double.parseDouble(args[1]);

        SigmaClient.purseSpoofer.coins.set(value);

        SigmaClient.sendMessageWithPrefix(String.format("Purse spoofed to %,.1f coins", SigmaClient.purseSpoofer.coins.getValue()));

        SigmaClient.configManager.saveConfig();
    }

    @Override
    public String getDescription() {
        return ".setpurse <value>";
    }
}