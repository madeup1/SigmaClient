package git.sigmaclient.modules.protection;

import git.sigmaclient.SigmaClient;
import git.sigmaclient.events.SessionAccessEvent;
import git.sigmaclient.modules.Module;

public class AntiRat extends Module
{
    public AntiRat()
    {
        super("Anti Rat", Category.PROTECTIONS);
    }

    @Override
    public void assign()
    {
        SigmaClient.antiRat = this;
    }

    public static boolean session(SessionAccessEvent event)
    {
        if (!SigmaClient.antiRat.isToggled())
            return false;

        StackTraceElement element = event.elements()[2];

        for (AllowedAccesses accesses : AllowedAccesses.values())
        {
            if (accesses.compare(element.getClassName(), element.getLineNumber(), element.getMethodName(), element.getFileName()))
            {
                return false;
            }
        }

        return true;
    }

    public static enum AllowedAccesses
    {
        NETLOGIN("net.minecraft.client.network.NetHandlerLoginClient", 70, "func_147389_a", "NetHandlerLoginClient.java");

        private final String clasz;
        private final int line;
        private final String method;
        private final String file;
        AllowedAccesses(String clasz, int line, String method, String file)
        {
            this.clasz = clasz;
            this.line = line;
            this.method = method;
            this.file = file;
        }

        public boolean compare(String clasz, int line, String method, String file)
        {
            if (this.clasz.equals(clasz)
                    && this.line == line
                    && this.method.equals(method)
                    && this.file.equals(file))
            {
                return true;
            }

            return false;
        }
    }
}
